package com.yiling.settlement.b2b.listener;

import javax.sql.rowset.serial.SerialException;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.mq.MessageListener;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.settlement.b2b.component.B2bOrderReceiveBus;
import com.yiling.settlement.b2b.service.SettlementOrderSyncService;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: dexi.yao
 * @date: 2022-04-08
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_B2B_ORDER_RECEIVE_STATE_SEND, consumerGroup = Constants.TAG_B2B_ORDER_RECEIVE_STATE_SEND)
public class B2bOrderReceiveListener extends AbstractMessageListener {

    @Autowired
    B2bOrderReceiveBus b2bOrderReceiveBus;

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;


//    @MdcLog
//    @Override
//    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
//        String msg = null;
//        try {
//            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
//            if (StrUtil.isBlank(msg)){
//                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 原因:{OrderCode为空}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
//                return MqAction.CommitMessage;
//            }
//            Boolean isCreate = b2bOrderReceiveBus.orderReceiveMsg(msg);
//            if (!isCreate){
//                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{isCreate=False}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
//                return MqAction.ReconsumeLater;
//            }
//            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
//        } catch (Exception e) {
//            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg, e);
//            return MqAction.ReconsumeLater;
//        }
//        return MqAction.CommitMessage;
//    }

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        Boolean isCreate = b2bOrderReceiveBus.orderReceiveMsg(body);
        if (!isCreate){
            log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 异常信息:{isCreate=False}", message.getMsgId(), message.getTopic(), message.getTags(), body);
            throw new ServiceException(ResultCode.FAILED);
        }
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), body);
        return MqAction.CommitMessage;
    }

    @Override
    protected int getMaxReconsumeTimes() {
        return 3;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {
        return (body, message, context, e) -> {
            mqMessageConsumeFailureApi.handleConsumeFailure(body, message, e);
            DubboUtils.quickAsyncCall("mqMessageConsumeFailureApi", "handleConsumeFailure");
        };
    }
}
