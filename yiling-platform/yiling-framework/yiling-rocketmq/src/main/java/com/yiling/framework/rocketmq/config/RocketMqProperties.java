package com.yiling.framework.rocketmq.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import lombok.Data;

/**
 * RocketMQ的配置文件
 *
 * @author xuan.zhou
 * @date 2020/8/13
 **/
@Data
@Configuration
@Order(-1)
public class RocketMqProperties {

    @Value("${rocketmq.groupName}")
    private String groupName;

    @Value("${rocketmq.namesrvAddr}")
    private String namesrvAddr;

    @Value("${rocketmq.producer.maxMessageSize}")
    private Integer maxMessageSize ;

    @Value("${rocketmq.producer.sendMsgTimeout}")
    private Integer sendMsgTimeout;

    @Value("${rocketmq.producer.retryTimesWhenSendFailed}")
    private Integer retryTimesWhenSendFailed;

    @Value("${rocketmq.consumer.maxReconsumeTimes}")
    private Integer maxReconsumeTimes;
}
