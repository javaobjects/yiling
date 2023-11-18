package com.yiling.framework.rocketmq.mq;

import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import com.yiling.framework.rocketmq.enums.MqAction;

/**
 * 有顺序消息监听
 *
 * @author xuan.zhou
 * @date 2020/8/13
 **/
public interface MessageOrderListener {

    /**
     * MQ 消费接口
     */
    MqAction consume(MessageExt message, ConsumeOrderlyContext context);
}
