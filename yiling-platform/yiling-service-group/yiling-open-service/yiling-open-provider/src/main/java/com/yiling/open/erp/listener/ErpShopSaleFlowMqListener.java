package com.yiling.open.erp.listener;

import java.text.MessageFormat;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.open.erp.dto.ErpShopSaleFlowDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.handler.ErpShopSaleFlowHandler;
import com.yiling.open.erp.util.ErpConstants;
import com.yiling.open.util.LogUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 连锁门店销售流向消息消费监听
 *
 * @author: houjie.sun
 * @date: 2023/3/22
 */
@Slf4j
@RocketMqListener(topic = "erp_shop_sale_flow", consumerGroup = "erp_shop_sale_flow")
public class ErpShopSaleFlowMqListener implements MessageListener {

    @Autowired
    private ErpShopSaleFlowHandler erpShopSaleFlowHandler;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            LogUtil.info(MessageFormat.format(ErpConstants.MQ_LISTENER_SUCCESS, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg));
            erpShopSaleFlowHandler.handleShopSaleFlowMqSync(Long.parseLong(message.getTags()), ErpTopicName.ErpShopSaleFlow.getMethod(), JSONArray.parseArray(msg, ErpShopSaleFlowDTO.class));
        } catch (Exception e) {
            log.error(MessageFormat.format(ErpConstants.MQ_LISTENER_FAILE, message.getMsgId(), message.getTopic(), message.getTags(), message.getKeys(), msg), e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }

}
