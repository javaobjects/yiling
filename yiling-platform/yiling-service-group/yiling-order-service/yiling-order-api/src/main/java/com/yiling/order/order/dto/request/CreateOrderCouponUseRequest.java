package com.yiling.order.order.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto.request
 * @date: 2021/10/27
 */
@Data
@Accessors(chain = true)
public class CreateOrderCouponUseRequest extends BaseRequest {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 活动ID
     */
    private Long couponActivityId;

    /**
     * 优惠劵ID
     */
    private Long couponId;

    /**
     * 优惠劵名称
     */
    private String couponName;

    /**
     * 实际使用金额
     */
    private BigDecimal amount;

    /**
     * 平台承担比例
     */
    private BigDecimal platformRatio;

    /**
     * 商家承担比例
     */
    private BigDecimal businessRatio;

    /**
     * 优惠劵类型(1,平台劵,2,商家劵)
     */
    private Integer couponType;
}
