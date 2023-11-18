package com.yiling.mall.payment.service.impl;

import java.util.Objects;
import java.util.Optional;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.mall.payment.service.PaymentDaysOrderService;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.request.UpdatePaymentOrderAmountRequest;

/**
 * 账期订单 ServiceImpl
 *
 * @author: xuan.zhou
 * @date: 2021/6/29
 */
@Service
public class PaymentOrderServiceImpl implements PaymentDaysOrderService {

    @DubboReference
    OrderApi orderApi;
    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;


    @Override
    public Boolean updatePaymentOrderAmount(Long orderId) {
        OrderDTO orderDTO = Optional.ofNullable(orderApi.getOrderInfo(orderId))
                .orElseThrow(() -> new BusinessException(OrderErrorCode.ORDER_NOT_EXISTS));

        UpdatePaymentOrderAmountRequest request = new UpdatePaymentOrderAmountRequest();
        request.setOrderId(orderId);

        return paymentDaysAccountApi.updatePaymentOrderAmount(request);
    }
}
