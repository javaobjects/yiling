package com.yiling.hmc.payment.mq.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.PostConstruct;

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
import com.yiling.hmc.order.api.MarketOrderApi;
import com.yiling.hmc.order.dto.MarketOrderDTO;
import com.yiling.hmc.order.enums.HmcOrderErrorCode;
import com.yiling.hmc.order.enums.HmcPaymentStatusEnum;
import com.yiling.hmc.wechat.dto.request.HmcDiagnosisOrderPaySuccessNotifyRequest;
import com.yiling.hmc.wechat.dto.request.MarketOrderPayNotifyRequest;
import com.yiling.payment.enums.AppOrderStatusEnum;
import com.yiling.payment.enums.TradeTypeEnum;
import com.yiling.payment.pay.dto.PayOrderDTO;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/** 以岭健康管理中心支付订单回调通知
 * @author zhigang.guo
 * @date: 2023/2/15
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_HMC_ORDER_PAY_NOTIFY, consumerGroup = Constants.TOPIC_HMC_ORDER_PAY_NOTIFY)
public class OrderPayFinishMessageListener extends AbstractMessageListener {

    @DubboReference(async = true)
    MqMessageConsumeFailureApi mqMessageConsumeFailureApi;
    @DubboReference
    MarketOrderApi marketOrderApi;

    private Map<TradeTypeEnum, Function<PayOrderDTO,MqAction>> callBackFunctionMap = new HashMap<>(3);


    @PostConstruct
    private void init () {
        callBackFunctionMap.put(TradeTypeEnum.PAY,this::marketOrderCallBack);
        callBackFunctionMap.put(TradeTypeEnum.INQUIRY,this::inquiryOrderCallBack);
        callBackFunctionMap.put(TradeTypeEnum.PRESCRIPTION,this::marketOrderCallBack);
    }

    @Override
    @MdcLog
    protected MqAction consume(String msg, MessageExt message, ConsumeConcurrentlyContext context) {

        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);
        PayOrderDTO payOrderDTO = JSON.parseObject(msg, PayOrderDTO.class);

        if (ObjectUtil.isNull(payOrderDTO)) {
            log.error("通知数据为空");
            return MqAction.CommitMessage;
        }

        // 支付失败不用修改状态
        if (AppOrderStatusEnum.SUCCESS !=  AppOrderStatusEnum.getByCode(payOrderDTO.getAppOrderStatus())) {

            return MqAction.CommitMessage;
        }

        TradeTypeEnum tradeTypeEnum = TradeTypeEnum.getByCode(payOrderDTO.getTradeType());

        if (tradeTypeEnum == null) {

            throw new RuntimeException("支付回调失败,交易类型错误!");
        }

        Function<PayOrderDTO, MqAction> mqActionFunction = callBackFunctionMap.get(tradeTypeEnum);

        if (mqActionFunction == null) {

            throw new RuntimeException("支付回调失败,交易类型错误!");
        }

        return mqActionFunction.apply(payOrderDTO);
    }


    /**
     * 市场订单支付&处理方订单回调
     * @param payOrderDTO
     * @return
     */
    private MqAction marketOrderCallBack(PayOrderDTO payOrderDTO) {

        MarketOrderDTO marketOrderDTO = Optional.ofNullable(marketOrderApi.queryById(payOrderDTO.getAppOrderId())).orElseThrow(() -> new BusinessException(HmcOrderErrorCode.ORDER_NOT_EXISTS));

        // 订单已支付成功,无需重复处理
        if (HmcPaymentStatusEnum.PAYED == HmcPaymentStatusEnum.getByCode(marketOrderDTO.getPaymentStatus())) {

            log.warn("重复回调,订单已支付成功!");

            return MqAction.CommitMessage;
        }

        MarketOrderPayNotifyRequest request = new MarketOrderPayNotifyRequest();
        request.setMerTranNo(payOrderDTO.getPayNo());
        request.setOrderId(payOrderDTO.getAppOrderId());
        request.setPayTime(payOrderDTO.getPayDate());
        request.setThirdPayNo(payOrderDTO.getThirdTradeNo());
        request.setThirdPartyTranNo(payOrderDTO.getBankNo());
        request.setThirdPayAmount(payOrderDTO.getPayAmount());

        // 回调通知订单支付状态
        Boolean isSuccess = marketOrderApi.payNotify(request);

        if (isSuccess) {

            return MqAction.CommitMessage;
        }

        return MqAction.ReconsumeLater;
    }


    /**
     * 问诊订单
     * @param payOrderDTO
     * @return
     */
    private MqAction inquiryOrderCallBack(PayOrderDTO payOrderDTO) {

        HmcDiagnosisOrderPaySuccessNotifyRequest request = new HmcDiagnosisOrderPaySuccessNotifyRequest();
        request.setDiagnosisRecordId(payOrderDTO.getAppOrderId().intValue());
        request.setMerTranNo(payOrderDTO.getPayNo());
        request.setTotalAmount(payOrderDTO.getGoodsAmount().toString());
        request.setBuyerPayAmount(payOrderDTO.getPayAmount().toString());
        request.setThirdPartyTranNo(payOrderDTO.getBankNo());
        request.setBankTranNo(payOrderDTO.getThirdTradeNo());

        Boolean result = marketOrderApi.diagnosisOrderPayNotify(request);

        if (result) {

            return MqAction.CommitMessage;
        }

        return MqAction.ReconsumeLater;
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
