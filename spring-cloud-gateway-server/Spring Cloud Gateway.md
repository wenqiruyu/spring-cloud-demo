### 简介

​      Spring Cloud网关提供了一个基于Spring生态系统的API网关，其中包括：Spring 5，Spring Boot 2和项目Reactor。Spring Cloud网关的目的是提供一种简单而有效的方法来路由到API，并向它们提供跨领域的关注，例如：安全性，监视/度量和弹性。

​      原来Spring Cloud是集成Netflix公司旗下的Zuul进行作为网关使用的，只是Zuul1.X是使用的阻塞式API，并不支持长链接，基于的是同步IO没有提供异步支持，在性能的表现上不是很理想。在经历Zuul2.X频繁的跳票了之后，Spring Cloud进行了孵化了自己的网关项目，也就是Spring Cloud Gateway，大概在18年6月进行替换Zuul1.X集成到Spring Cloud中。Spring Cloud Gateway可以看作一个Zuul 1.x的升级版和代替品。

### Gateway的作用

- 实现统一接入、动态的路由、权限认证、数据缓存

- 进行对流量的控制，实现服务的熔断、降级、限流

- 性能的监控、日志监控、灰度发布

- IP黑名单，URL黑名单  风控防刷、预防恶意攻击

### 核心概念

**路由 Route**

> 网关的基本构件。它由ID、目标URI、断言集合和过滤器集合定义。如果聚合的断言为true，则匹配路由。

**断言 Predicate** 

> 这是 Java 8 Function谓词。输入类型为Spring Framework `ServerWebExchange`。这使开发人员可以匹配HTTP请求中的任何内容，例如消息头或参数。

**过滤器 Filter** 

> 这些是使用特定工厂构造的Spring Framework `GatewayFilter`实例。在此，可以在发送下游请求之前或之后修改请求和响应。

### 工作原理

![Spring Cloud网关图](https://www.springcloud.cc/images/spring_cloud_gateway_diagram.png)

### 创建Gateway项目

引用Spring Cloud Gateway依赖：

```xml
<!--Gateway网关依赖-->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

> Spring Cloud Gateway使用的是Spring Boot和Spring Webflux提供的Netty底层环境，不能和传统的Servlet容器一起使用，所以项目中是不能引入Web依赖的,不然将会报错，而且也不能打包成一个WAR包

项目添加YML配置文件：

```yml
server:
  port: 10530 # 指定项目端口

spring:
  application:
    name: gateway-server # 制定项目名称
  cloud:
    gateway:
      routes:
        - id: user-server # 网关路由ID，除需唯一无其他特殊要求
          uri: http://localhost:10529 # 网关路由的URI
          predicates: # 网关路由断言集
            - Path=/usr/{name}
```

启动下游**user-server**项目（服务端口为10529），启动**gateway-server**网关项目（服务端口为10530）

浏览器访问http://localhost:10530/usr/yingx

![image-20210323231648890](C:\Users\22341\AppData\Roaming\Typora\typora-user-images\image-20210323231648890.png)

可以看到，访问的是**10530**端口网关服务，网关将该请求路由转发到了**user-server**服务的**/usr/{name}**接口上，获得了返回结果

### 限流机制

常用的限流算法包括：**令牌桶限流，漏桶限流，计数器限流**；

> **令牌桶限流**
> 		令牌桶算法的原理是系统以一定速率向桶中放入令牌，填满了就丢弃令牌；请求来时会先从桶中取出令牌，如果能取到令牌，则可以继续完成请求，否则等待或者拒绝服务；令牌桶允许一定程度突发流量，只要有令牌就可以处理，支持一次拿多个令牌；
> **漏桶限流**
> 		漏桶算法的原理是按照固定常量速率流出请求，流入请求速率任意，当请求数超过桶的容量时，新的请求等待或者拒绝服务；可以看出漏桶算法可以强制限制数据的传输速度；
> **计数器限流**
> 		计数器是一种比较简单粗暴的算法，主要用来限制总并发数，比如数据库连接池、线程池、秒杀的并发数；计数器限流只要一定时间内的总请求数超过设定的阀值则进行限流；

具体基于以上算法如何实现，Guava提供了RateLimiter工具类基于基于令牌桶算法：

```java
 RateLimiter rateLimiter = RateLimiter.create(5);
```


以上代码表示一秒钟只允许处理五个并发请求，以上方式只能用在单应用的请求限流，不能进行全局限流；这个时候就需要分布式限流，可以基于redis+lua来实现；