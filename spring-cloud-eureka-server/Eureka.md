### 禁用Eureka Discovery Client
可使用以下配置进行禁用:
eureka.client.enabled=false
spring.cloud.discovery.enabled=false
eureka.client.register-with-eureka=false

### Eureka的安全验证
http://user:password@localhost:8761/eureka

要使EurekaServer注册中心拥有简单的安全验证功能的话,只需通过在注册中心中添加**spring-boot-starter-security**依赖,
将Spring Security添加到注册中心中即可保护Eureka服务器。但在默认情况下，当Spring Security存在注册中心时，它将要求在每次向应用程序发送请求时都发送有效的CSRF令牌。
但是Eureka客户通常不会拥有有效的跨站点请求伪造（CSRF）令牌，所以需要为/eureka/**端点禁用此要求。
```java
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/eureka/**");
        super.configure(http);
    }
}
```

### 更改EurekaClient客户端注册的实例ID
EurekaClient客户端进行注册到EurekaServer注册中心的实例ID默认为:
**${spring.cloud.client.hostname}:${spring.application.name}:${spring.application.instance_id:${server.port}}}**
例如: **hostname:spring-cloud-eureka-client:9999**

如果有需求可进行更改配置:
```yaml
eureka:
  instance:
    instanceId: *
```


1.两个注解@EnableDiscoveryClient和@EnableEurekaClient
@EnableEurekaClient只能使用在Eureka,标识该服务是一个EurekaClient客户端,并成为**eureka.client.serviceUrl.defaultZone**配置的Url配
置的EurekaServer注册中心的实例,能够被其他服务发现
@EnableDiscoveryClient可以使用于其他的注册组件,如Zookeeper(Zookeeper是Apache Hadoop的子项目，适合作为Dubbo服务的注册中心),
Consul(Google开源的一个使用go语言开发的服务发现、配置管理中心服务)等

2.当前版本下在不使用@EnableEurekaClient注解或者@EnableDiscoveryClient注解的情况下服务也是能注册到配置的注册中心的
针对这个问题可以查看下
1)@EnableEurekaClient的源码,如下:
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface EnableEurekaClient {
}
2)@EnableDiscoveryClient的源码,如下:
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({EnableDiscoveryClientImportSelector.class})
public @interface EnableDiscoveryClient {
boolean autoRegister() default true;
}
可以看出该注解中没有用到任何的@Import解决,已经可以说@EnableEurekaClient没有任何作用了.

1)那么现版本EurekaClient是怎么实现自动启用服务的注册发现功能的
找到引入的依赖 spring-cloud-netflix-eureka-client 中的META-INF文件夹下的spring.factories文件,如下:
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.cloud.netflix.eureka.config.EurekaClientConfigServerAutoConfiguration,\
org.springframework.cloud.netflix.eureka.config.DiscoveryClientOptionalArgsConfiguration,\
org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration,\
org.springframework.cloud.netflix.ribbon.eureka.RibbonEurekaAutoConfiguration,\
org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration,\
org.springframework.cloud.netflix.eureka.reactive.EurekaReactiveDiscoveryClientConfiguration,\
org.springframework.cloud.netflix.eureka.loadbalancer.LoadBalancerEurekaAutoConfiguration

org.springframework.cloud.bootstrap.BootstrapConfiguration=\
org.springframework.cloud.netflix.eureka.config.EurekaConfigServerBootstrapConfiguration
其中有个关键的类:EurekaClientAutoConfiguration
关注:eurekaAutoServiceRegistration方法将EurekaAutoServiceRegistration注入到Spring容器中
EurekaAutoServiceRegistration类实现了SmartLifecycle类,当Spring容器加载所有bean并完成初始化之后，会接着回调实现该接口的类中对应的start方法
Lifecycle接口类定义启动/停止生命周期控制方法，当spring ioc容器启动或停止时将发送一个启动或者停止的信号通知到各个组件
EurekaAutoServiceRegistration类中的start方法中 this.serviceRegistry.register(this.registration); 调用EurekaServiceRegistry.register实现向注册中心进行注册的操作.
由此EurekaAutoServiceRegistration类使用了Lifecycle类的机制在Spring容器加载完所有的Bean并完成初始化后,执行start方法进行服务的自动注册.

在Spring Cloud Edgware版本开始,@EnableEurekaClient注解或者@EnableDiscoveryClient注解可省略
