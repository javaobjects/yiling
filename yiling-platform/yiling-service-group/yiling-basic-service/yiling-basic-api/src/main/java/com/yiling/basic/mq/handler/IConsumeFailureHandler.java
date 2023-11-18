package com.yiling.basic.mq.handler;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 消费失败处理器接口
 * 
 * @author: xuan.zhou
 * @date: 2022/1/26
 */
public interface IConsumeFailureHandler {

    void handler(String body, MessageExt message, ConsumeConcurrentlyContext context, Exception e);
}
