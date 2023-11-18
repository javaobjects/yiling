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
import com.yiling.open.erp.dto.ErpGoodsCustomerPriceDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.handler.ErpGoodsCustomerPriceHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息消费监听
 * @author shuan
 */
@Slf4j
@RocketMqListener(topic = "erp_goods_customer_price", consumerGroup = "erp_goods_customer_price")
public class ErpGoodsCustomerPriceMqListener implements MessageListener {

    @Autowired
    private ErpGoodsCustomerPriceHandler erpGoodsCustomerPriceHandler;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
            erpGoodsCustomerPriceHandler.handleGoodsCustomerPriceMqSync(Long.parseLong(message.getTags()), ErpTopicName.ErpGoodsPrice.getMethod(), JSONArray.parseArray(msg, ErpGoodsCustomerPriceDTO.class));
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }
}
