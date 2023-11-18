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
import com.yiling.open.erp.dto.ErpGoodsGroupPriceDTO;
import com.yiling.open.erp.enums.ErpTopicName;
import com.yiling.open.erp.handler.ErpGoodsGroupPriceHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 消息消费监听
 * @author shuan
 */
@Slf4j
@RocketMqListener(topic = "erp_goods_group_price", consumerGroup = "erp_goods_group_price")
public class ErpGoodsGroupPriceMqListener implements MessageListener {

    @Autowired
    private ErpGoodsGroupPriceHandler erpGoodsGroupPriceHandler;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
            erpGoodsGroupPriceHandler.handleGoodsGroupPriceMqSync(Long.parseLong(message.getTags()), ErpTopicName.ErpGroupPrice.getMethod(), JSONArray.parseArray(msg, ErpGoodsGroupPriceDTO.class));
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }
}
