package com.yiling.mall.payment.service;


import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.Test;

import com.yiling.mall.BaseTest;
import com.yiling.mall.payment.api.PaymentDaysOrderApi;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lun.yu
 * @date: 2021/8/10
 */
@Slf4j
public class PaymentOrderServiceTest extends BaseTest {

    @DubboReference
    PaymentDaysOrderApi paymentDaysOrderApi;

    @Test
    public void test () {
        paymentDaysOrderApi.updatePaymentOrderAmount(124L);
    }

}
