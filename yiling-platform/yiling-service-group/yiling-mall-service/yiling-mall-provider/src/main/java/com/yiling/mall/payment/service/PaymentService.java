package com.yiling.mall.payment.service;

import java.math.BigDecimal;

import com.yiling.mall.payment.dto.request.PaymentRequest;

/**
 * 支付 Service
 *
 * @author: xuan.zhou
 * @date: 2021/6/29
 */
public interface PaymentService {

    /**
     * 线下支付
     *
     * @param request
     * @return
     */
    boolean pay(PaymentRequest request);

    /**
     * 计算订单现折金额
     * @param paymentRequest
     * @return
     */
    BigDecimal calculateOrderCashDiscountAmount(PaymentRequest.OrderPaymentRequest paymentRequest);

}
