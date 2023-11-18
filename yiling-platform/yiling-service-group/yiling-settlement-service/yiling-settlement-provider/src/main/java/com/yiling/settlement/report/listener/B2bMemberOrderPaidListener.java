package com.yiling.settlement.report.listener;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
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
import com.yiling.settlement.report.entity.MemberSyncDO;
import com.yiling.settlement.report.service.MemberSyncService;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: dexi.yao
 * @date: 2022-04-08
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_BUY_B2B_MEMBER_SUCCESS, consumerGroup = Constants.TOPIC_BUY_B2B_MEMBER_SUCCESS)
public class B2bMemberOrderPaidListener extends AbstractMessageListener {

    @Autowired
    MemberSyncService memberSyncService;

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;

//    @MdcLog
//    @Override
//    public MqAction consume(MessageExt message, ConsumeConcurrentlyContext context) {
//        String msg = null;
//        try {
//            msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
//            if (StrUtil.isBlank(msg)) {
//                log.error("MsgId:{}, 应用MQ消费失败, Topic:{}, Tag:{}，Body:{}, 原因:{OrderCode为空}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
//                return MqAction.CommitMessage;
//            }
////            JSONObject object = JSONObject.parseObject(msg);
//
//            MemberSyncDO syncDO = JSONObject.parseObject(msg, MemberSyncDO.class);
////            syncDO.setAmount(object.getBigDecimal("originalPrice"));
//
//            Boolean isCreate = memberSyncService.createMemberOrderSync(syncDO);
//            if (!isCreate) {
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

        MemberSyncDO syncDO = JSONObject.parseObject(body, MemberSyncDO.class);

        Boolean isCreate = memberSyncService.createMemberOrderSync(syncDO);
        if (!isCreate) {
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
