package com.yiling.order.order.api;

import com.yiling.order.order.dto.request.CreateOrderPayMentMethodRequest;

/** 订单支付方式api
 * @author zhigang.guo
 * @date: 2022/10/12
 */
public interface OrderPaymentMethodApi {

    /**
     * 保存订单支付方式
     * @param payMentMethodRequest
     */
    Boolean save(CreateOrderPayMentMethodRequest payMentMethodRequest);
}
