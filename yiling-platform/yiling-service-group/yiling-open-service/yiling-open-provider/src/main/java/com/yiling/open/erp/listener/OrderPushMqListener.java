package com.yiling.open.erp.listener;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.goods.medicine.dto.GoodsRefreshDTO;
import com.yiling.open.erp.service.ErpGoodsCustomerPriceService;
import com.yiling.open.erp.service.ErpGoodsGroupPriceService;
import com.yiling.open.erp.service.ErpGoodsService;
import com.yiling.open.erp.service.ErpOrderPushService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/1/12
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_ORDER_PUSH_ERP, consumerGroup = Constants.TOPIC_ORDER_PUSH_ERP)
public class OrderPushMqListener implements MessageListener {

    @Autowired
    private ErpOrderPushService erpOrderPushService;

    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            List<Long> orderIds = JSONArray.parseArray(msg, Long.class);
            erpOrderPushService.erpOrderPush(orderIds);
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }
}
