package com.wen.eureka.cloud.spring.config;

import com.netflix.appinfo.InstanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.eureka.server.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 项目名称：fint_iia_phosphor_server
 * 类名称：EurekaStateChangeListener
 * 类描述：服务注册EurekaServer服务的各种状态的处理
 * 创建人：yingx
 * 创建时间：2020/9/21
 * 修改人：yingx
 * 修改时间：2020/9/21
 * 修改备注：
 */
@Component
public class EurekaStateChangeListener {

    private static Logger logger = LoggerFactory.getLogger(EurekaStateChangeListener.class);

    /**
     * 失效服务进行剔除
     * (处理重复监听)
     *
     * @param event
     * @return void
     * @author yingx
     * @date 2020/9/21
     */
    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceCanceledEvent event) {
        // 服务断线事件
        String appName = event.getAppName();
        String serverId = event.getServerId();

        // 可以进行扩展消息提醒 邮件、手机短信、微信等及将相关记录存储Redis

        logger.error("服务：{} - {} 出现错误，已下线......", serverId, appName);
    }

    /**
     * 服务成功注册到EurekaServer注册中心
     *
     * @param event
     * @return void
     * @author yingx
     * @date 2020/9/21
     */
    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceRegisteredEvent event) {
        // 服务注册
        InstanceInfo instanceInfo = event.getInstanceInfo();
        String appName = instanceInfo.getAppName();
        String instanceId = instanceInfo.getInstanceId();

        logger.info("服务：{} - {} 成功注册到注册中心......", instanceId, appName);
    }

    /**
     * 服务进行续约
     *
     * @param event
     * @return void
     * @author yingx
     * @date 2020/9/21
     */
    @EventListener(condition = "#event.replication==false")
    public void listen(EurekaInstanceRenewedEvent event) {
        // 服务续约
        logger.info("服务：{} - {} 进行续约成功......", event.getServerId(), event.getAppName());
    }

    /**
     * EurekaServer注册中心可用状态
     *
     * @param event
     * @return void
     * @author yingx
     * @date 2020/9/21
     */
    @EventListener
    public void listen(EurekaRegistryAvailableEvent event) {
        // 注册中心启动
        logger.info("EurekaServer注册中心存活......");
    }

    /**
     * 启动EurekaServer注册中心
     *
     * @param event
     * @return void
     * @author yingx
     * @date 2020/9/21
     */
    @EventListener
    public void listen(EurekaServerStartedEvent event) {
        // Server启动
        logger.info("EurekaServer注册中心启动成功......");
    }
}
