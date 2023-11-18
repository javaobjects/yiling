package com.yiling.framework.rocketmq.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;

import lombok.extern.slf4j.Slf4j;


/**
 * spring 启动成功后初始化消费者
 *
 * @author xuan.zhou
 * @date 2020/8/13
 **/
@Slf4j
public class RocketMqConsumerRunner implements ApplicationRunner {

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        RocketMqConsumer consumer = context.getBean(RocketMqConsumer.class);
        consumer.start();
    }
}