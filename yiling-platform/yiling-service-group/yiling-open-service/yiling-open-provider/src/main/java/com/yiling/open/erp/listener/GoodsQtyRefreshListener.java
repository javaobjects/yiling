package com.yiling.open.erp.listener;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.goods.medicine.dto.GoodsRefreshDTO;
import com.yiling.open.erp.service.ErpOrderSendService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/1/12
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_GOODS_QTY_REFRESH_SEND, consumerGroup = Constants.TOPIC_GOODS_QTY_REFRESH_SEND)
public class GoodsQtyRefreshListener implements MessageListener {

    @Autowired
    private ErpOrderSendService erpOrderSendService;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            GoodsRefreshDTO goodsRefreshDTO = JSON.parseObject(msg, GoodsRefreshDTO.class);
            erpOrderSendService.refreshErpInventoryList(goodsRefreshDTO.getInSnList(),Long.parseLong(String.valueOf(goodsRefreshDTO.getEid())));
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }
}
