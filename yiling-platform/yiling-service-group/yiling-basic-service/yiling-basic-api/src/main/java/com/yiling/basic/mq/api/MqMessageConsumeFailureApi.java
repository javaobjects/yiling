package com.yiling.basic.mq.api;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * mq消费失败的消息 API
 *
 * @author: xuan.zhou
 * @date: 2022/1/26
 */
public interface MqMessageConsumeFailureApi {

    /**
     * 该方法已过时。<br/>
     * 原因：context参数没有序列化，导致api调用报错。<br/>
     * 解决方案：使用 handleConsumeFailure(String body, MessageExt message, Exception e) 方法替换。<br/>
     *
     * @param body
     * @param message
     * @param context
     * @param e
     */
    @Deprecated
    void handleConsumeFailure(String body, MessageExt message, ConsumeConcurrentlyContext context, Exception e);

    void handleConsumeFailure(String body, MessageExt message, Exception e);
}
