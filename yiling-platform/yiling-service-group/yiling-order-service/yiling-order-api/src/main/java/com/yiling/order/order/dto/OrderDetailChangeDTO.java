package com.yiling.order.order.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单明细变更信息
 * @author:wei.wang
 * @date:2021/8/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderDetailChangeDTO extends BaseDTO {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单明细ID
     */
    private Long detailId;

    /**
     * 商品标准库ID
     */
    private Long standardId;

    /**
     * 商品SKUID
     */
    private Long goodsSkuId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品ERP编码
     */
    private String goodsErpCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;

    /**
     * 购买数量
     */
    private Integer goodsQuantity;

    /**
     * 购买商品小计
     */
    private BigDecimal goodsAmount;

    /**
     * 购买商品现折金额
     */
    private BigDecimal cashDiscountAmount;

    /**
     * 购买商品票折金额
     */
    private BigDecimal ticketDiscountAmount;

    /**
     * 平台优惠劵折扣金额
     */
    private BigDecimal platformCouponDiscountAmount;

    /**
     * 商家优惠劵折扣金额
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

    /**
     * 发货数量
     */
    private Integer deliveryQuantity;

    /**
     * 发货商品小计
     */
    private BigDecimal deliveryAmount;

    /**
     * 发货商品的现折金额
     */
    private BigDecimal deliveryCashDiscountAmount;

    /**
     * 发货商品的票折金额
     */
    private BigDecimal deliveryTicketDiscountAmount;

    /**
     * 发货商品的平台优惠劵折扣金额
     */
    private BigDecimal deliveryPlatformCouponDiscountAmount;

    /**
     * 发货商品的商家优惠劵金额
     */
    private BigDecimal deliveryCouponDiscountAmount;

    /**
     * 发货商品的预售优惠金额
     */
    private BigDecimal deliveryPresaleDiscountAmount;

    /**
     * 发货商品的平台支付优惠金额
     */
    private BigDecimal deliveryPlatformPaymentDiscountAmount;

    /**
     * 发货商品的商家支付优惠金额
     */
    private BigDecimal deliveryShopPaymentDiscountAmount;

    /**
     * 卖家退货数量
     */
    private Integer sellerReturnQuantity;

    /**
     * 卖家退货商品小计
     */
    private BigDecimal sellerReturnAmount;

    /**
     * 卖家退货商品的现折金额
     */
    private BigDecimal sellerReturnCashDiscountAmount;

    /**
     * 卖家退货商品的票折金额
     */
    private BigDecimal sellerReturnTicketDiscountAmount;

    /**
     * 卖家退货商品的平台优惠劵折扣金额
     */
    private BigDecimal sellerPlatformCouponDiscountAmount;

    /**
     * 卖家退货商品的商家优惠劵金额
     */
    private BigDecimal sellerCouponDiscountAmount;


    /**
     * 卖家退货商品的预售优惠金额
     */
    private BigDecimal sellerPresaleDiscountAmount;

    /**
     * 卖家退货商品的平台支付优惠金额
     */
    private BigDecimal sellerPlatformPaymentDiscountAmount;

    /**
     * 卖家退货商品的商家支付优惠金额
     */
    private BigDecimal  sellerShopPaymentDiscountAmount;

    /**
     * 收货数量
     */
    private Integer receiveQuantity;

    /**
     * 收货商品小计
     */
    private BigDecimal receiveAmount;

    /**
     * 收货商品的现折金额
     */
    private BigDecimal receiveCashDiscountAmount;

    /**
     * 收货商品的票折金额
     */
    private BigDecimal receiveTicketDiscountAmount;

    /**
     * 收货商品的商家优惠劵金额
     */
    private BigDecimal receiveCouponDiscountAmount;

    /**
     * 收货平台的商家优惠劵金额
     */
    private BigDecimal receivePlatformCouponDiscountAmount;

    /**
     * 收货商品的预售优惠金额
     */
    private BigDecimal receivePresaleDiscountAmount;

    /**
     * 收货商品的平台支付优惠金额
     */
    private BigDecimal receivePlatformPaymentDiscountAmount;

    /**
     * 收货商品的商家支付优惠金额
     */
    private BigDecimal receiveShopPaymentDiscountAmount;

    /**
     * 退货数量
     */
    private Integer returnQuantity;

    /**
     * 退货商品小计
     */
    private BigDecimal returnAmount;

    /**
     * 退货商品的现折金额
     */
    private BigDecimal returnCashDiscountAmount;

    /**
     * 退货商品的票折金额
     */
    private BigDecimal returnTicketDiscountAmount;

    /**
     * 退货商品的平台优惠劵折扣金额
     */
    private BigDecimal returnPlatformCouponDiscountAmount;

    /**
     * 退货商品的商家优惠劵折扣金额
     */
    private BigDecimal returnCouponDiscountAmount;

    /**
     * 退货商品的预售优惠金额
     */
    private BigDecimal returnPresaleDiscountAmount;


    /**
     * 退货商品的平台支付优惠金额
     */
    private BigDecimal returnPlatformPaymentDiscountAmount;

    /**
     * 退货商品的商家支付优惠金额
     */
    private BigDecimal returnShopPaymentDiscountAmount;
}
