package com.yiling.settlement.b2b.listener;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.settlement.b2b.component.B2bOrderReceiveBus;
import com.yiling.settlement.b2b.service.SettlementOrderCancelService;
import com.yiling.settlement.b2b.service.SettlementOrderSyncService;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: dexi.yao
 * @date: 2022-04-08
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_B2B_PRESALE_ORDER_CANCEL_SEND, consumerGroup = Constants.TAG_B2B_PRESALE_ORDER_CANCEL_SEND)
public class B2bPresaleOrderCancelListener implements MessageListener {

    @Autowired
    SettlementOrderSyncService settlementOrderSyncService;


    @MdcLog
    @Override
    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = null;
        try {
            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            if (StrUtil.isBlank(msg)){
                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 原因:{OrderCode为空}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
                return MqAction.CommitMessage;
            }
            Boolean isCreate = settlementOrderSyncService.createSettOrderCancelSync(msg);
            if (!isCreate){
                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{isCreate=False}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
                return MqAction.ReconsumeLater;
            }
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        } catch (Exception e) {
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
            return MqAction.ReconsumeLater;
        }
        return MqAction.CommitMessage;
    }
}
