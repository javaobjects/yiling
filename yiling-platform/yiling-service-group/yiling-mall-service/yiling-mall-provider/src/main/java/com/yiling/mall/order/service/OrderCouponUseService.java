package com.yiling.mall.order.service;

import com.yiling.mall.order.dto.request.OrderCouponUseReturnRequest;

/**
 * @author: houjie.sun
 * @date: 2021/11/19
 */

public interface OrderCouponUseService{

    /**
     * 退还优惠券
     * @param request
     * @return
     */
    Boolean orderReturnCoupon(OrderCouponUseReturnRequest request);
}
