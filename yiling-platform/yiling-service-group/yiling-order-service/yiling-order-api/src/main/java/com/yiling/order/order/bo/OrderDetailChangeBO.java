package com.yiling.order.order.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 订单（发货、收货、退货）操作导致的订单明细变更记录 BO
 *
 * @author: xuan.zhou
 * @date: 2021/8/4
 */
@Data
public class OrderDetailChangeBO implements java.io.Serializable {

    /**
     * 订单明细ID
     */
    private Long detailId;

    /**
     * 操作数量（发货、收货、退货）
     */
    private Integer quantity;

    /**
     * 涉及商品金额
     */
    private BigDecimal amount;

    /**
     * 涉及商品现折金额
     */
    private BigDecimal cashDiscountAmount;

    /**
     * 涉及商品票折金额
     */
    private BigDecimal ticketDiscountAmount;

    /**
     * 涉及平台优惠劵折扣金额
     */
    private BigDecimal platformCouponDiscountAmount;

    /**
     * 涉及商家优惠劵折扣金额
     */
    private BigDecimal couponDiscountAmount;

    /**
     * 预售优惠金额
     */
    private BigDecimal presaleDiscountAmount;


    /**
     * 平台支付优惠总金额
     */
    private BigDecimal platformPaymentDiscountAmount;

    /**
     * 商家支付优惠金额
     */
    private BigDecimal shopPaymentDiscountAmount;
}
