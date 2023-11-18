package com.yiling.open.erp.listener;

import java.text.MessageFormat;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.open.erp.handler.ErpShopSaleFlowHandler;
import com.yiling.open.erp.util.ErpConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * 连锁门店销售流向解封消息消费监听
 *
 * @author: houjie.sun
 * @date: 2023/03/22
 */
@Slf4j
@RocketMqListener(topic = "erp_flow_sealed_unLock_shop_sale", consumerGroup = "erp_flow_sealed_unLock_shop_sale")
public class ErpFlowSealedUnLockShopSaleMqListener implements MessageListener {

    @Autowired
    private ErpShopSaleFlowHandler erpShopSaleFlowHandler;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info(MessageFormat.format(ErpConstants.MQ_LISTENER_SUCCESS, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg));
            erpShopSaleFlowHandler.handleUnLockShopSaleFlowMqSync(Long.parseLong(message.getTags()), msg);
        } catch (Exception e) {
            log.info(MessageFormat.format(ErpConstants.MQ_LISTENER_FAILE, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg), e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }

}
