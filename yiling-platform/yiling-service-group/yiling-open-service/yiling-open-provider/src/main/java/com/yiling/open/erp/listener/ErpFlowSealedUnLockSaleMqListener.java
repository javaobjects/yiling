package com.yiling.open.erp.listener;

import java.text.MessageFormat;

import com.yiling.open.erp.util.ErpConstants;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.open.erp.handler.ErpSaleFlowHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 销售流向解封消息消费监听
 *
 * @author: houjie.sun
 * @date: 2022/04/24
 */
@Slf4j
@RocketMqListener(topic = "erp_flow_sealed_unLock_sale", consumerGroup = "erp_flow_sealed_unLock_sale")
public class ErpFlowSealedUnLockSaleMqListener implements MessageListener {

    @Autowired
    private ErpSaleFlowHandler erpSaleFlowHandler;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info(MessageFormat.format(ErpConstants.MQ_LISTENER_SUCCESS, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg));
            erpSaleFlowHandler.handleUnLockSaleFlowMqSync(Long.parseLong(message.getTags()), msg);
        } catch (Exception e) {
            log.info(MessageFormat.format(ErpConstants.MQ_LISTENER_FAILE, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg), e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }

}
