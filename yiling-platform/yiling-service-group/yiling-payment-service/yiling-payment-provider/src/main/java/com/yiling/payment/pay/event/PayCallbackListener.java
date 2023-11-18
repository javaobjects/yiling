package com.yiling.payment.pay.event;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.payment.enums.AppOrderStatusEnum;
import com.yiling.payment.enums.OrderPlatformEnum;
import com.yiling.payment.enums.RefundStateEnum;
import com.yiling.payment.pay.entity.PaymentMergeOrderDO;
import com.yiling.payment.pay.entity.PaymentRepeatOrderDO;
import com.yiling.payment.pay.entity.PaymentTradeDO;
import com.yiling.payment.pay.service.PaymentMergeOrderService;
import com.yiling.payment.pay.service.PaymentRepeatOrderService;
import com.yiling.payment.pay.service.PaymentTradeService;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 支付回调监听
 *
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.event
 * @date: 2021/12/31
 */
@Component
@Slf4j
public class PayCallbackListener {
    @Autowired
    private PaymentRepeatOrderService paymentRepeatOrderService;
    @Autowired
    private PaymentTradeService paymentTradeService;
    @Autowired
    private PaymentMergeOrderService paymentMergeOrderService;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;


    /**
     * 大运河支付成功通知消息
     * @param payCallbackEvent
     */
    @EventListener(condition = "#payCallbackEvent.isRepeatPay == false")
    @Order(1)
    public void b2bPayCallback(PayCallbackEvent payCallbackEvent) {

        // 大运河支付成功消息
        List<MqMessageBO> mqMessageBOList = payCallbackEvent.getPayOrderList().stream().filter(t -> OrderPlatformEnum.B2B == OrderPlatformEnum.getByCode(t.getOrderPlatform())).map(payOrderDTO -> {// 支付成功消息
            MqMessageBO payMessageBo = new MqMessageBO(Constants.TOPIC_ORDER_PAY_NOTIFY, "", JSON.toJSONString(payOrderDTO));
            payMessageBo = mqMessageSendApi.prepare(payMessageBo);
            return payMessageBo;

        }).collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(mqMessageBOList)) {

            log.info("..b2bPayCallback...{}", JSON.toJSON(payCallbackEvent.getPayOrderList()));

            mqMessageBOList.forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));
        }
    }

    /**
     * 以岭健康管理中心支付消息
     * @param payCallbackEvent
     */
    @EventListener(condition = "#payCallbackEvent.isRepeatPay == false")
    @Order(2)
    public void hmcPayCallback(PayCallbackEvent payCallbackEvent) {

        //以岭健康管理中心支付成功消息
        List<MqMessageBO> mqMessageBOList = payCallbackEvent.getPayOrderList().stream().filter(t -> OrderPlatformEnum.HMC == OrderPlatformEnum.getByCode(t.getOrderPlatform())).map(payOrderDTO -> {// 支付成功消息
            MqMessageBO payMessageBo = new MqMessageBO(Constants.TOPIC_HMC_ORDER_PAY_NOTIFY, "", JSON.toJSONString(payOrderDTO));
            payMessageBo = mqMessageSendApi.prepare(payMessageBo);

            return payMessageBo;

        }).collect(Collectors.toList());

        if (CollectionUtil.isNotEmpty(mqMessageBOList)) {

            log.info("..hmcPayCallback...{}", JSON.toJSON(payCallbackEvent.getPayOrderList()));

            mqMessageBOList.forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));
        }
    }


    /**
     * 支付完成回调通知,校验是否支付重复
     *
     * @param payCallbackEvent
     */
    @EventListener(condition = "#payCallbackEvent.isRepeatPay == false")
    @Order(4)
    public void finishPayCallback(PayCallbackEvent payCallbackEvent) {
        if (log.isDebugEnabled()) {

            log.debug("..finishPayCallback...{}", JSON.toJSON(payCallbackEvent));
        }

        List<MqMessageBO> mqMessageBOList = payCallbackEvent.getPayOrderList().stream().filter(payOrderDTO -> AppOrderStatusEnum.SUCCESS == AppOrderStatusEnum.getByCode(payOrderDTO.getAppOrderStatus())).map(t -> {
            // 支付重复校验消息
            MqMessageBO payMessageBo = new MqMessageBO(Constants.TOPIC_ORDER_PAY_REPEAT_NOTIFY, "", JSON.toJSONString(t));
            payMessageBo = mqMessageSendApi.prepare(payMessageBo);

            return payMessageBo;

        }).collect(Collectors.toList());

        mqMessageBOList.forEach(mqMessageBO -> mqMessageSendApi.send(mqMessageBO));

    }

    /**
     * 通一个预订单，选择多种支付，支付完成生成的重复订单
     *
     * @param payCallbackEvent
     */
    @EventListener(condition = "#payCallbackEvent.isRepeatPay == true")
    @Order(3)
    @Async
    public void repeatPayCallback(PayCallbackEvent payCallbackEvent) {
        if (log.isDebugEnabled()) {

            log.debug("..repeatPayCallback...{}", JSON.toJSON(payCallbackEvent));
        }

        payCallbackEvent.getPayOrderList().stream().forEach(t -> {
            PaymentMergeOrderDO mergeOrderDO = PojoUtils.map(t, PaymentMergeOrderDO.class);
            this.createRepeatOrder(mergeOrderDO);
        });
    }

    /**
     * 自动创建退款单
     *
     * @param paymentMergeOrderDO
     * @return
     */
    public boolean createRepeatOrder(PaymentMergeOrderDO paymentMergeOrderDO) {
        if (log.isDebugEnabled()) {

            log.debug("createRepeatOrder....request-> {}", JSON.toJSON(paymentMergeOrderDO));
        }

        List<PaymentRepeatOrderDO> repeatOrderDOList = paymentRepeatOrderService.selectRepeatOrderByPayNo(paymentMergeOrderDO.getPayNo(), paymentMergeOrderDO.getAppOrderNo());
        // 防止创建重复，如果存在退款单，不用创建
        if (CollectionUtil.isNotEmpty(repeatOrderDOList)) {
            return true;
        }
        PaymentTradeDO paymentTradeDO = paymentTradeService.selectPaymentTradeByPayNo(paymentMergeOrderDO.getPayNo());
        if (paymentMergeOrderDO == null) {
            return true;
        }

        PaymentMergeOrderDO updateMergeOrder = new PaymentMergeOrderDO();
        updateMergeOrder.setId(paymentMergeOrderDO.getId());
        updateMergeOrder.setIsDuplicate(1);

        // 标记订单为重复支付订单
        paymentMergeOrderService.updateById(updateMergeOrder);

        paymentMergeOrderDO.setId(null);
        paymentMergeOrderDO.setAppOrderStatus(AppOrderStatusEnum.SUCCESS.getCode());
        paymentMergeOrderDO.setRefundState(1);
        paymentMergeOrderDO.setRefundAmount(BigDecimal.ZERO);
        paymentMergeOrderService.save(paymentMergeOrderDO);
        PaymentRepeatOrderDO paymentRepeatOrderDO = new PaymentRepeatOrderDO();
        paymentRepeatOrderDO.setAppOrderId(paymentMergeOrderDO.getAppOrderId());
        paymentRepeatOrderDO.setAppOrderNo(paymentMergeOrderDO.getAppOrderNo());
        paymentRepeatOrderDO.setRefundState(RefundStateEnum.WAIT_REFUND.getCode());
        paymentRepeatOrderDO.setRefundAmount(paymentMergeOrderDO.getPayAmount());
        paymentRepeatOrderDO.setPayId(paymentMergeOrderDO.getPayId());
        paymentRepeatOrderDO.setReason("支付重复,退款");
        paymentRepeatOrderDO.setPayNo(paymentMergeOrderDO.getPayNo());
        paymentRepeatOrderDO.setThirdTradeNo(paymentTradeDO.getThirdTradeNo());
        paymentRepeatOrderDO.setOperateTime(new Date());
        paymentRepeatOrderDO.setOperateUser(0l);
        return paymentRepeatOrderService.insertRepeatOrder(paymentRepeatOrderDO);
    }
}
