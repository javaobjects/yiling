package com.yiling.mall.payment.api;

import java.math.BigDecimal;

import com.yiling.mall.payment.dto.request.PaymentRequest;

/**
 * 支付 API
 *
 * @author: xuan.zhou
 * @date: 2021/6/29
 */
public interface PaymentApi {

    /**
     * 支付
     *
     * @param request
     * @return
     */
    boolean pay(PaymentRequest request);


    /**
     *  计算订单现折金额
     * @param request
     * @return
     */
    BigDecimal calculateOrderCashDiscountAmount(PaymentRequest.OrderPaymentRequest request);

}
