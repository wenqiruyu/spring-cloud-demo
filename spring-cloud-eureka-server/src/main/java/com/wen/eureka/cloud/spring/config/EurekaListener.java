// package com.wen.cloud.spring.config;
//
// import com.netflix.discovery.shared.Applications;
// import com.netflix.eureka.EurekaServerContextHolder;
// import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.cloud.netflix.eureka.server.event.*;
// import org.springframework.context.ApplicationEvent;
// import org.springframework.context.ApplicationListener;
// import org.springframework.context.annotation.Configuration;
//
// /**
//  * 项目名称：fint_iia_phosphor_server
//  * 类名称：VenusEurekaListener
//  * 类描述：服务注册EurekaServer服务的各种状态的处理
//  * 创建人：yingx
//  * 创建时间：2020/9/21
//  * 修改人：yingx
//  * 修改时间：2020/9/21
//  * 修改备注：
//  */
// @Configuration
// public class EurekaListener implements ApplicationListener<ApplicationEvent> {
//
//     private static final Logger logger = LoggerFactory.getLogger(EurekaListener.class);
//
//     @Override
//     public void onApplicationEvent(ApplicationEvent applicationEvent) {
//
//         // 1、服务下线
//         if (applicationEvent instanceof EurekaInstanceCanceledEvent) {
//             // 服务出现错误的实例
//             EurekaInstanceCanceledEvent event = (EurekaInstanceCanceledEvent) applicationEvent;
//             // 获取当前Eureka实例中的节点信息
//             PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
//             Applications applications = registry.getApplications();
//             // 遍历获取已注册节点中与当前失效节点ID一致的节点信息
//             applications.getRegisteredApplications().forEach(registeredApplication -> {
//                 registeredApplication.getInstances().forEach(instance -> {
//                     if (instance.getInstanceId().equals(event.getServerId())) {
//                         // 可以进行扩展消息提醒 邮件、手机短信、微信等
//                         logger.error("服务：{} 出现错误，已下线......", instance.getAppName());
//                     }
//                 });
//             });
//         }
//
//         // 2、服务成功注册到EurekaServer注册中心
//         if (applicationEvent instanceof EurekaInstanceRegisteredEvent) {
//             EurekaInstanceRegisteredEvent event = (EurekaInstanceRegisteredEvent) applicationEvent;
//             logger.info("服务：{} 成功注册到注册中心......", event.getInstanceInfo().getAppName());
//         }
//
//         // 3、服务进行续约
//         if (applicationEvent instanceof EurekaInstanceRenewedEvent) {
//             EurekaInstanceRenewedEvent event = (EurekaInstanceRenewedEvent) applicationEvent;
//             logger.info("服务：{} 进行续约成功......", event.getInstanceInfo().getAppName());
//         }
//
//         // 4、EurekaServer注册中心可用状态
//         if (applicationEvent instanceof EurekaRegistryAvailableEvent) {
//             logger.info("EurekaServer注册中心存活......");
//         }
//
//         // 5、启动EurekaServer注册中心
//         if (applicationEvent instanceof EurekaServerStartedEvent) {
//             logger.info("EurekaServer注册中心启动成功......");
//         }
//     }
// }
