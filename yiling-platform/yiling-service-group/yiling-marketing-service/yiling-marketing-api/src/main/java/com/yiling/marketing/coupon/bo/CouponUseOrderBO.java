package com.yiling.marketing.coupon.bo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 优惠券使用信息类
 *
 * @author: houjie.sun
 * @date: 2022/4/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CouponUseOrderBO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠劵ID
     */
    private Long couponId;

    /**
     * 活动ID
     */
    private Long couponActivityId;

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 优惠劵ID
     */
    private Integer getType;

    /**
     * 获取时间
     */
    private Date getTime;

    /**
     * 用券时间
     */
    private Date useTime;

    /**
     * 获取人ID
     */
    private Long userId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 实际使用金额
     */
    private BigDecimal amount;

}
