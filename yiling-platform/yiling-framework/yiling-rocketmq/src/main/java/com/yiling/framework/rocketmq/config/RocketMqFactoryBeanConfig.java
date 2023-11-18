package com.yiling.framework.rocketmq.config;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.yiling.framework.rocketmq.mq.RocketMqConsumer;

/**
 * 创建生产者和消费者的工厂bean
 *
 * @author xuan.zhou
 * @date 2020/8/13
 **/
public class RocketMqFactoryBeanConfig {

    @Bean
    public RocketMqConsumer createConsumer(RocketMqProperties configuration, ApplicationContext context) {
        return new RocketMqConsumer(configuration, context);
    }

    @Bean
    public DefaultMQProducer defaultProducer(RocketMqProperties configuration) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer(configuration.getGroupName());
        producer.setNamesrvAddr(configuration.getNamesrvAddr());
        producer.setInstanceName(System.currentTimeMillis() + "");

        Integer maxMessageSize = configuration.getMaxMessageSize();
        if (maxMessageSize != null) {
            producer.setMaxMessageSize(maxMessageSize);
        }

        Integer sendMsgTimeout = configuration.getSendMsgTimeout();
        if (sendMsgTimeout != null) {
            producer.setSendMsgTimeout(sendMsgTimeout);
        }

        //如果发送消息失败，设置重试次数，默认为2次
        Integer retryTimesWhenSendFailed = configuration.getRetryTimesWhenSendFailed();
        if (retryTimesWhenSendFailed != null) {
            producer.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);
        }

        producer.start();
        return producer;
    }

}
