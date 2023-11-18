package com.yiling.open.erp.listener;

import java.text.MessageFormat;

import com.yiling.open.erp.util.ErpConstants;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.open.erp.dto.ErpPurchaseFlowDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.handler.ErpPurchaseFlowHandler;
import com.yiling.open.util.LogUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 客户信息消息消费监听
 *
 * @author: houjie.sun
 * @date: 2021/8/30
 */
@Slf4j
@RocketMqListener(topic = "erp_purchase_flow", consumerGroup = "erp_purchase_flow")
public class ErpPurchaseFlowMqListener implements MessageListener {

    @Autowired
    private ErpPurchaseFlowHandler erpPurchaseFlowHandler;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            LogUtil.info(MessageFormat.format(ErpConstants.MQ_LISTENER_SUCCESS, message.getMsgId(), message.getTopic(), message.getTags(),message.getKeys(), msg));
            erpPurchaseFlowHandler.handlePurchaseFlowMqSync(Long.parseLong(message.getTags()), ErpTopicName.ErpPurchaseFlow.getMethod(), JSONArray.parseArray(msg, ErpPurchaseFlowDTO.class));
        } catch (Exception e) {
            log.info(MessageFormat.format(ErpConstants.MQ_LISTENER_FAILE, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg), e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }

}