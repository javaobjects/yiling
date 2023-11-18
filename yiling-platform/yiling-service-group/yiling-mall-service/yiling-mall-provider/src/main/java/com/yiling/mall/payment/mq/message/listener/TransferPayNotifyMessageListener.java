package com.yiling.mall.payment.mq.message.listener;

import java.util.Collections;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.payment.enums.TradeStatusEnum;
import com.yiling.payment.pay.dto.PaymentTransferResultDTO;
import com.yiling.settlement.b2b.api.SettlementApi;
import com.yiling.settlement.b2b.dto.request.UpdatePaymentStatusRequest;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/** 企业打款回调监听
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.mall.order.mq.message.listener
 * @date: 2021/11/22
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_TRANSFER_PAY_NOTIFY, consumerGroup = Constants.TOPIC_TRANSFER_PAY_NOTIFY)
public class TransferPayNotifyMessageListener extends AbstractMessageListener {
    @DubboReference
    SettlementApi settlementApi;
    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;


    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {

        String appOrderNo = null;
        try {
            String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
            log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
            PaymentTransferResultDTO transferResultDTO =  JSON.parseObject(msg, PaymentTransferResultDTO.class);
            if (ObjectUtil.isNull(transferResultDTO)) {
                log.error("通知数据为空");
                return MqAction.CommitMessage;
            }

            appOrderNo = transferResultDTO.getPayNo();
            UpdatePaymentStatusRequest request = new UpdatePaymentStatusRequest();
            request.setPaymentStatus(transferResultDTO.getTradeStatusEnum() != null ? transferResultDTO.getTradeStatusEnum().getCode():TradeStatusEnum.BANK_ING.getCode());
            request.setErrMsg(transferResultDTO.getErrMSg());
            request.setPayNo(appOrderNo);
            request.setThirdPayNo(transferResultDTO.getThirdTradeNo());
            Boolean result = settlementApi.updatePaymentStatus(Collections.singletonList(request));
            if (result) {
                return MqAction.CommitMessage;
            } else {
                log.error("结算信息订单已支付：{}", appOrderNo);
                return MqAction.ReconsumeLater;
            }
        } catch (BusinessException e) {

            log.debug("[{}],errorMsg:{}",appOrderNo,e.getMessage());

            return MqAction.CommitMessage;

        }
    }

    @Override
    protected int getMaxReconsumeTimes() {

        return 7;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {

        return (body, message, context, e) -> {
            mqMessageConsumeFailureApi.handleConsumeFailure(body, message, e);
            DubboUtils.quickAsyncCall("mqMessageConsumeFailureApi", "handleConsumeFailure");
        };
    }
}
