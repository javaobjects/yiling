package com.yiling.order.order.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.order.order.api.OrderPaymentMethodApi;
import com.yiling.order.order.dto.request.CreateOrderPayMentMethodRequest;
import com.yiling.order.order.service.OrderPaymentMethodService;

import lombok.extern.slf4j.Slf4j;

/** 订单支付方式
 * @author zhigang.guo
 * @date: 2022/10/12
 */
@Slf4j
@DubboService
public class OrderPaymentMethodApiImpl implements OrderPaymentMethodApi {
    @Autowired
    private OrderPaymentMethodService orderPaymentMethodService;

    @Override
    public Boolean save(CreateOrderPayMentMethodRequest payMentMethodRequest) {

        return orderPaymentMethodService.save(payMentMethodRequest);
    }
}
