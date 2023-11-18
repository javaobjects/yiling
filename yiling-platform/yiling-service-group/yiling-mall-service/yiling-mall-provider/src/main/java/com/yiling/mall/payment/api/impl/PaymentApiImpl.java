package com.yiling.mall.payment.api.impl;

import java.math.BigDecimal;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.mall.payment.api.PaymentApi;
import com.yiling.mall.payment.dto.request.PaymentRequest;
import com.yiling.mall.payment.service.PaymentService;

/**
 * 支付 API 实现
 *
 * @author: xuan.zhou
 * @date: 2021/6/29
 */
@DubboService
public class PaymentApiImpl implements PaymentApi {
    @Autowired
    PaymentService                  paymentService;

    @Override
    public boolean pay(PaymentRequest request) {

        return paymentService.pay(request);
    }

    @Override
    public BigDecimal calculateOrderCashDiscountAmount(PaymentRequest.OrderPaymentRequest request) {

        return paymentService.calculateOrderCashDiscountAmount(request);
    }
}
