package com.yiling.payment.pay.mq.message.listener;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.basic.mq.handler.IConsumeFailureHandler;
import com.yiling.basic.mq.listener.AbstractMessageListener;
import com.yiling.framework.common.log.MdcLog;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.rocketmq.annotation.RocketMqListener;
import com.yiling.framework.rocketmq.enums.MqAction;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;
import com.yiling.framework.rocketmq.util.MqMsgConvertUtil;
import com.yiling.payment.channel.service.PayFactory;
import com.yiling.payment.channel.service.PayService;
import com.yiling.payment.channel.service.dto.PayOrderResultDTO;
import com.yiling.payment.channel.service.dto.request.QueryPayOrderRequest;
import com.yiling.payment.channel.service.dto.request.RetryRequest;
import com.yiling.payment.enums.AppOrderStatusEnum;
import com.yiling.payment.enums.PaymentErrorCode;
import com.yiling.payment.enums.TradeStatusEnum;
import com.yiling.payment.pay.api.PayApi;
import com.yiling.payment.pay.dto.request.PayCallBackRequest;
import com.yiling.payment.pay.entity.PaymentTradeDO;
import com.yiling.payment.pay.service.PaymentTradeService;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

/** 延时查询订单支付状态
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.mq.message.listener
 * @date: 2021/11/2
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_PAY_ORDER_QUERY, consumerGroup = Constants.TOPIC_PAY_ORDER_QUERY)
public class ParkOrderQueryHandlerListener extends AbstractMessageListener {

    @Autowired
    private PayFactory              payFactory;
    @Autowired
    private PayApi                  payApi;
    @Autowired
    private PaymentTradeService     paymentTradeService;
    @DubboReference(async = true)
    MqMessageConsumeFailureApi      mqMessageConsumeFailureApi;
    @DubboReference
    MqMessageSendApi                mqMessageSendApi;
    @Autowired
    private RedisService            redisService;


    @Override
    @MdcLog
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {

        String msg = MqMsgConvertUtil.bytes2String(message.getBody(), "UTF-8");

        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), msg);

        if (StringUtils.isEmpty(msg)) {
            log.error("交易ID为空");
            return MqAction.CommitMessage;
        }

        RetryRequest<QueryPayOrderRequest> retryRequest =  JSONObject.parseObject(msg,new TypeReference<RetryRequest<QueryPayOrderRequest>>(){});

        if (ObjectUtil.isNull(retryRequest) || ObjectUtil.isNull(retryRequest.getData())) {
            log.warn("通知数据为空");
            return MqAction.CommitMessage;
        }

        QueryPayOrderRequest request = retryRequest.getData();

        if (log.isDebugEnabled()) {

            log.debug("预订单payNo:{}延迟查询...delayTimeLevel:{}",request.getThirdTradeNo(),message.getDelayTimeLevel());
        }

        PaymentTradeDO paymentTradeDO = paymentTradeService.selectPaymentTradeByPayNo(request.getPayNo());

        if (paymentTradeDO == null) {

            return MqAction.CommitMessage;
        }

        if (HttpStatus.HTTP_OK != checkOrderRedisStatus(paymentTradeDO.getPayId()).getCode()) {

            return MqAction.CommitMessage;
        }

        // 拦截是否查询成功
        if ( TradeStatusEnum.SUCCESS  == TradeStatusEnum.getByCode(paymentTradeDO.getTradeStatus()) || TradeStatusEnum.CLOSE == TradeStatusEnum.getByCode(paymentTradeDO.getTradeStatus())) {

            return MqAction.CommitMessage;
        }

        PayService payService = payFactory.getPayInstance(paymentTradeDO.getPayWay(),paymentTradeDO.getPaySource());
        PayOrderResultDTO result = payService.orderQuery(request);

        if (result.getIsSuccess()) {

            Integer tradeState = result.getIsSuccess() ? TradeStatusEnum.SUCCESS.getCode() : TradeStatusEnum.FALIUE.getCode();
            PayCallBackRequest callBackRequest = PayCallBackRequest
                    .builder()
                    .payNo(result.getOutTradeNo())
                    .payWay(result.getPayWay())
                    .thirdId(result.getTradeNo())
                    .tradeState(tradeState)
                    .thirdState(result.getThirdState())
                    .thirdPaySource(result.getPaySource())
                    // 成功交易日期
                    .tradeDate(result.getTradeDate())
                    .bank(StringUtils.isNotBlank(result.getThird_party_tran_no()) ? result.getThird_party_tran_no() : "")
                    .build();
            // 通知订单支付成功
            payApi.operationCallBackThird(callBackRequest);

            return MqAction.CommitMessage;
        }

        // 发送重试任务
        this.sendDelayRetry(retryRequest,message);

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


    /**
     * 重试推送消息
     * @param retryMessage
     */
    private void sendDelayRetry(RetryRequest<QueryPayOrderRequest> retryMessage,MessageExt message) {

        int currentCount;

        retryMessage.setCurrentCount(currentCount = retryMessage.getCurrentCount() + 1);
        // 重试达到最大次数,或者重试次数
        if (currentCount > retryMessage.getTotalCount() || message.getReconsumeTimes() >= retryMessage.getTotalCount()) {

            log.warn("{} - 达到最大次数-{}, 停止重试! retryMessage: {}", retryMessage.getTopic(), retryMessage.getTotalCount(), JSON.toJSONString(retryMessage));
            return;
        }

        if (log.isDebugEnabled()) {

            log.debug("{} - 发送重试消息-{}/{}, retryMessage: {}", retryMessage.getTopic(), retryMessage.getCurrentCount(), retryMessage.getTotalCount(), JSON.toJSONString(retryMessage));
        }

        Integer delayLevel = Integer.parseInt(retryMessage.getDelayLevels().split(",")[retryMessage.getCurrentCount() - 1]);

        if (delayLevel != null) {

            // 发送延迟消息,查询订单的状态
            MqMessageBO payMessageBo = new MqMessageBO(retryMessage.getTopic(),"",JSONObject.toJSONString(retryMessage),  MqDelayLevel.getByLevel(delayLevel));
            payMessageBo = mqMessageSendApi.prepare(payMessageBo);
            mqMessageSendApi.send(payMessageBo);
        }
    }


    /**
     * 校验redis中支付记录的状态,无效请求拦截
     * @param payId
     * @return
     */
    private Result checkOrderRedisStatus(String payId) {
        String redis_order_status = RedisKey.generate("payment", "order", "payId");

        Object appOrderStatus = redisService.get(redis_order_status + RedisKey.SYMBOL + payId);
        // 校验缓存中订单支付状态,防止无效请求
        if (ObjectUtil.isNotNull(appOrderStatus)) {
            AppOrderStatusEnum appOrderStatusEnum  = AppOrderStatusEnum.getByCode(Integer.valueOf(appOrderStatus.toString()));
            switch (appOrderStatusEnum) {
                case SUCCESS :
                    return Result.failed(PaymentErrorCode.ORDER_PAID_ERROR);
                case CLOSE:
                    return Result.failed("交易已取消，请重新发起支付!");
                default: break;
            }
        }
        return Result.success();
    }

}
