package com.yiling.open.erp.listener;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.open.erp.dto.ErpOrderSendDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.handler.ErpOrderSendHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息消费监听
 * @author shuan
 */
@Slf4j
@RocketMqListener(topic = "erp_order_send", consumerGroup = "erp_order_send")
public class ErpOrderSendMqListener implements MessageListener {

    @Autowired
    private ErpOrderSendHandler erpOrderSendHandler;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
            erpOrderSendHandler.handleOrderSendMqSync(Long.parseLong(message.getTags()), ErpTopicName.ErpOrderSend.getMethod(), JSONArray.parseArray(msg, ErpOrderSendDTO.class));
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }
}
