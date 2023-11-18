//package com.yiling.admin.gateway.listener;
//
//import javax.annotation.PostConstruct;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
//import org.springframework.cloud.gateway.route.CachingRouteLocator;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.RouteRefreshListener;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.context.ApplicationEventPublisherAware;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.nacos.api.naming.listener.Event;
//import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
//import com.alibaba.nacos.common.notify.NotifyCenter;
//import com.alibaba.nacos.common.notify.listener.Subscriber;
//
///**
// * 监听Nacos InstancesChangeEvent事件，并刷新路由
// *
// * @author xuan.zhou
// * @date 2022/3/1
// */
//@Component
//public class InstancesChangeEventListener extends Subscriber<InstancesChangeEvent> implements ApplicationEventPublisherAware {
//
//    private static final Logger logger = LoggerFactory.getLogger(InstancesChangeEventListener.class);
//
//    private ApplicationEventPublisher applicationEventPublisher;
//
//    @Autowired
//    private RouteRefreshListener routeRefreshListener;
//    @Autowired
//    private ApplicationContext applicationContext;
//    @Autowired
//    private RouteLocator routeLocator;
//
//    @PostConstruct
//    private void post() {
//        NotifyCenter.registerSubscriber(this);
//    }
//
//
//    /**
//     * Event callback.
//     *
//     * @param event {@link Event}
//     */
//    @Override
//    public void onEvent(InstancesChangeEvent event) {
//        logger.info("接收到 InstancesChangeEvent 订阅事件：{}", JSON.toJSONString(event));
//        publishEvent();
//    }
//
//    /**
//     * Type of this subscriber's subscription.
//     *
//     * @return Class which extends {@link Event}
//     */
//    @Override
//    public Class<? extends com.alibaba.nacos.common.notify.Event> subscribeType() {
//        return InstancesChangeEvent.class;
//    }
//
//    public void publishEvent() {
//        CachingRouteLocator cachingRouteLocator = (CachingRouteLocator) routeLocator;
//        cachingRouteLocator.refresh();
//
//        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(new Object()));
//
//        routeRefreshListener.onApplicationEvent(new ContextRefreshedEvent(applicationContext));
//        cachingRouteLocator.onApplicationEvent(new RefreshRoutesEvent(new Object()));
//    }
//
//    @Override
//    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
//        this.applicationEventPublisher = applicationEventPublisher;
//    }
//}
//
