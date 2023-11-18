package com.yiling.payment.pay.mq.message.listener;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageConsumeFailureApi;
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
import com.yiling.payment.channel.service.PayFactory;
import com.yiling.payment.channel.service.PayService;
import com.yiling.payment.channel.service.dto.TradeContentJsonDTO;
import com.yiling.payment.channel.service.dto.request.ClosePayOrderRequest;
import com.yiling.payment.enums.AppOrderStatusEnum;
import com.yiling.payment.enums.TradeStatusEnum;
import com.yiling.payment.pay.dto.request.CloseOrderRequest;
import com.yiling.payment.pay.entity.PaymentMergeOrderDO;
import com.yiling.payment.pay.entity.PaymentTradeDO;
import com.yiling.payment.pay.service.PaymentMergeOrderService;
import com.yiling.payment.pay.service.PaymentTradeService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * 订单交易关闭通知
 * @author zhigang.guo
 * @date: 2022/5/10
 */
@Slf4j
@RocketMqListener(topic = Constants.TOPIC_PAY_TRADE_CLOSE_NOTIFY, consumerGroup = Constants.TOPIC_PAY_TRADE_CLOSE_NOTIFY)
public class PayTradeCloseMessageListener extends AbstractMessageListener {
    @Autowired
    private PaymentMergeOrderService  paymentMergeOrderService;
    @Autowired
    private PaymentTradeService       paymentTradeService;
    @DubboReference(async = true)
    MqMessageConsumeFailureApi        mqMessageConsumeFailureApi;
    @Autowired
    private PayFactory                payFactory;
    @Autowired
    private RedisService              redisService;
    @Lazy
    @Autowired
    private PayTradeCloseMessageListener _this;


    /**
     * 关闭支付中第三方远程支付交易记录
     * @param paymentTradeIngList 关闭订单
     */
    @Async("asyncExecutor")
    public void closePaymentTradeIngList(List<PaymentTradeDO> paymentTradeIngList) {
        if (CollectionUtil.isEmpty(paymentTradeIngList)) {
            return;
        }
        for (PaymentTradeDO trade: paymentTradeIngList) {
            String merchantNo = trade.getContent() != null ? JSON.parseObject(trade.getContent(), TradeContentJsonDTO.class).getMerchantNo() : null ;
            // 关闭第三方支付交易记录
            PayService payService = payFactory.getPayInstance(trade.getPayWay(),trade.getPaySource());
            Result<Void> result = payService.closeOrder(ClosePayOrderRequest.builder().payNo(trade.getPayNo()).thirdTradeNo(trade.getThirdTradeNo()).merchantNo(merchantNo).build());
            if (HttpStatus.HTTP_OK != result.getCode()) {
                log.warn("取消订单异常:{}",result.getMessage());
                continue;
            }

            PaymentTradeDO updateTrade = new PaymentTradeDO();
            updateTrade.setPayNo(trade.getPayNo());
            updateTrade.setTradeStatus(TradeStatusEnum.CLOSE.getCode());
            paymentTradeService.updatePaymentTradeStatus(updateTrade);
        }
    }


    /**
     * 设置订单redis支付状态(2小时)
     * @param payId
     * @param appOrderStatusEnum
     */
    private void setOrderRedisStatus(String payId,AppOrderStatusEnum appOrderStatusEnum) {
        String   redis_order_status = RedisKey.generate("payment", "order", "payId");
        // 设置缓存支付结果
        redisService.set(redis_order_status + RedisKey.SYMBOL + payId,appOrderStatusEnum.getCode(),60 * 60 * 2);
    }

    @MdcLog
    @Override
    protected MqAction consume(String body, MessageExt message, ConsumeConcurrentlyContext context) {

        log.info("MsgId:{}, MQ消费, Topic:{}, Tag:{}，Body:{}", message.getMsgId(), message.getTopic(), message.getTags(), body);

        if (StringUtils.isEmpty(body)) {
            log.error("取消支付交易数据为空");
            return MqAction.CommitMessage;
        }

        CloseOrderRequest closePayOrderRequest =  JSON.parseObject(body, CloseOrderRequest.class);
        List<PaymentMergeOrderDO> resultList = paymentMergeOrderService.selectMergerOrderByOrderNoList(closePayOrderRequest.getOrderPlatform(),closePayOrderRequest.getAppOrderNo());
        if (CollectionUtil.isEmpty(resultList)) {
            log.info("查询结果为空");
            return MqAction.CommitMessage;
        }

        resultList = resultList.stream().filter(t -> AppOrderStatusEnum.WAIT_PAY == AppOrderStatusEnum.getByCode(t.getAppOrderStatus())
                || AppOrderStatusEnum.PAY_ING == AppOrderStatusEnum.getByCode(t.getAppOrderStatus())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(resultList)) {
            log.info("未查询到需要关闭的交易记录");
            return MqAction.CommitMessage;
        }

        // 关闭交易记录
        for (PaymentMergeOrderDO paymentMergeOrderDO : resultList ) {
            List<PaymentTradeDO> paymentTradeDOList =  paymentTradeService.selectPayIngTradeByPayId(paymentMergeOrderDO.getPayId());
            PaymentMergeOrderDO updateOrderDo = new PaymentMergeOrderDO();
            updateOrderDo.setId(paymentMergeOrderDO.getId());
            updateOrderDo.setAppOrderStatus(AppOrderStatusEnum.CLOSE.getCode());
            paymentMergeOrderService.updateById(updateOrderDo);
            // 设置缓存支付结果
            setOrderRedisStatus(paymentMergeOrderDO.getPayId(),AppOrderStatusEnum.CLOSE);
            // 如果存在支付中的交易记录，需要关闭第三方支付交易
            _this.closePaymentTradeIngList(paymentTradeDOList);
        }
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
