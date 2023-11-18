package com.yiling.mall.payment.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.mall.payment.api.PaymentDaysOrderApi;
import com.yiling.mall.payment.service.PaymentDaysOrderService;

/**
 * 账期订单 API 实现
 *
 * @author lun.yu
 * @date 2021/8/10
 */
@DubboService
public class PaymentDaysOrderApiImpl implements PaymentDaysOrderApi {

    @Autowired
    private PaymentDaysOrderService paymentDaysOrderService;

    @Override
    public Boolean updatePaymentOrderAmount(Long orderId) {
        return paymentDaysOrderService.updatePaymentOrderAmount(orderId);
    }
}
