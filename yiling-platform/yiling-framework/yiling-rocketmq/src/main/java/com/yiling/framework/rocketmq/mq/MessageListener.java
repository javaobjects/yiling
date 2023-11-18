package com.yiling.framework.rocketmq.mq;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import com.yiling.framework.rocketmq.enums.MqAction;

/**
 * 普通消息监听
 *
 * @author xuan.zhou
 * @date 2020/8/13
 **/
public interface MessageListener {

    /**
     * MQ 消费接口
     */
    MqAction consume(MessageExt message, ConsumeConcurrentlyContext context);
}
