package com.yiling.marketing.couponactivity.dto.request;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/13
 */
@Data
@Accessors(chain = true)
public class OrderUseCouponDetailRequest {

    /**
     * 优惠券ID
     */
    private Long couponId;

}
