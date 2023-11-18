package com.yiling.hmc.payment.mq.listener;

import java.util.Date;
import java.util.EnumSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.hmc.order.api.MarketOrderApi;
import com.yiling.hmc.wechat.dto.request.MarketOrderRefundNotifyRequest;
import com.yiling.ih.patient.api.HmcDiagnosisApi;
import com.yiling.ih.patient.dto.request.HmcCancelDiagnosisOrderNotifyRequest;
import com.yiling.payment.enums.RefundStateEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.dto.RefundOrderResultDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * 在线退款回调监听
 *
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.mall.order.mq.message.listener
 * @date: 2023/05/11
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_HMC_PAY_REFUND_NOTIFY, consumerGroup = Constants.TOPIC_HMC_PAY_REFUND_NOTIFY)
public class OrderPayReturnMessageListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @DubboReference
    HmcDiagnosisApi hmcDiagnosisApi;
    @DubboReference
    MarketOrderApi marketOrderApi;



    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {
        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");
        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);

        if (StringUtils.isBlank(msg)) {

            log.error("消息为空");
            return MqAction.CommitMessage;
        }

        RefundOrderResultDTO payOrderDTO = JSON.parseObject(msg, RefundOrderResultDTO.class);

        // C端退款目前只处理失败或者成功
        EnumSet<RefundStateEnum> refundStateEnumEnum = EnumSet.of(RefundStateEnum.SUCCESS, RefundStateEnum.FALIUE, RefundStateEnum.CLOSE);
        // 表示会员退款订单，需要调整会员退款记录状态
        if (!refundStateEnumEnum.contains(payOrderDTO.getRefundStateEnum())) {

            return MqAction.CommitMessage;
        }

        // c端问诊
        if (TradeTypeEnum.INQUIRY == payOrderDTO.getTradeTypeEnum()) {
             HmcCancelDiagnosisOrderNotifyRequest request = new HmcCancelDiagnosisOrderNotifyRequest();
             request.setDiagnosisRecordId(payOrderDTO.getAppOrderId().intValue());
             request.setDiagnosisRecordRefundId(payOrderDTO.getAppRefundId().intValue());
             request.setSysOrderNo(payOrderDTO.getThirdTradeNo());

             if (RefundStateEnum.SUCCESS == payOrderDTO.getRefundStateEnum()) {
                 // 退款状态 0成功 1失败
                 request.setCancelStatus(0);
             } else {
                 request.setCancelStatus(1);
             }

             Boolean recordNotify = hmcDiagnosisApi.cancelDiagnosisRecordNotify(request);

             if (!recordNotify) {

                 return MqAction.ReconsumeLater;
             }
            // c端处方
        } else if (TradeTypeEnum.PRESCRIPTION == payOrderDTO.getTradeTypeEnum()){

            MarketOrderRefundNotifyRequest notifyRequest = new MarketOrderRefundNotifyRequest();
            notifyRequest.setOrderId(payOrderDTO.getAppOrderId());
            notifyRequest.setRefundId(payOrderDTO.getAppRefundId());
            notifyRequest.setRefundTime(new Date());
            notifyRequest.setThirdPayNo(payOrderDTO.getThirdFundNo());
            // 退款没有第三方渠道退款号
            // notifyRequest.setThirdPartyTranNo(payOrderDTO.getBankNo());
            notifyRequest.setThirdPayAmount(payOrderDTO.getRealReturnAmount());
            notifyRequest.setSysOrderNo(payOrderDTO.getThirdTradeNo());

            if (RefundStateEnum.SUCCESS == payOrderDTO.getRefundStateEnum()) {
                // 退款状态 0成功 1失败
                notifyRequest.setCancelStatus(0);
            } else {
                notifyRequest.setCancelStatus(1);
            }

            Boolean recordNotify = marketOrderApi.prescriptionOrderRefundNotify(notifyRequest);

            if (!recordNotify) {

                return MqAction.ReconsumeLater;
            }
        }

        return MqAction.CommitMessage;
    }

    @Override
    protected int getMaxReconsumeTimes() {

        return 5;
    }

    @Override
    protected IConsumeFailureHandler getConsumeFailureHandler() {

        return (body, message, context, e) -> {
            mqMessageConsumeFailureApi.handleConsumeFailure(body, message, e);
            DubboUtils.quickAsyncCall("mqMessageConsumeFailureApi", "handleConsumeFailure");
        };
    }

}
