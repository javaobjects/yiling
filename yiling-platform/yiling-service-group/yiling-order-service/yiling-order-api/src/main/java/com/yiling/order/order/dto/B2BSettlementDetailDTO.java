package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto
 * @date: 2021/10/24
 */
@Data
@Accessors(chain = true)
public class B2BSettlementDetailDTO implements java.io.Serializable {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 明细Id
     */
    private Long detailId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 支付类型：1-线下支付 2-在线支付
     */
    private Integer paymentType;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    private Integer paymentMethod;

    /**
     * 在线支付渠道
     */
    private String payChannel;

	/**
	 * 下单时间
	 */
	private Date createTime;

	/**
	 * 促销活动ID(折扣价活动预留)
	 */
	private Long promotionActivityId;

	/**
	 * 促销活动类型
	 * @see com.yiling.order.order.enums.PromotionActivityTypeEnum
	 */
	private Integer promotionActivityType;

	/**
	 * 促销活动优惠小计=（originalPrice - goodsPrice） * goodsQuantity
	 */
	private BigDecimal promotionSaleSubTotal;

	/**
	 * 退货促销活动优惠小计=（originalPrice - goodsPrice） * (sellerReturnQuantity + returnQuantity)
	 */
	private BigDecimal returnPromotionSaleSubTotal;

    /**
     * 非以岭品   组合促销优惠金额小计=（originalPrice - goodsPrice） * goodsQuantity
     * 以岭品组   合促销优惠金额小计=（limitPrice - goodsPrice） * goodsQuantity
     */
    private BigDecimal comPacAmount;

    /**
     * 非以岭品   退回组合促销优惠的金额小计=（originalPrice - goodsPrice） * (sellerReturnQuantity + returnQuantity)
     * 以岭品组   退回组合促销优惠的金额小计=（limitPrice - goodsPrice） * (sellerReturnQuantity + returnQuantity)
     */
    private BigDecimal returnComPacAmount;

    /**
     * 退货商品的平台优惠劵折扣金额
     */
    private BigDecimal returnPlatformCouponDiscountAmount;

    /**
     * 退货商品的商家优惠劵折扣金额
     */
    private BigDecimal returnCouponDiscountAmount;

    /**
     * 退货商品预售金额
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

    /**
     * 收货商品的商家优惠劵金额
     */
    private BigDecimal receiveCouponDiscountAmount;

    /**
     * 收货平台的商家优惠劵金额
     */
    private BigDecimal receivePlatformCouponDiscountAmount;

    /**
     * 收货预售金额
     */
    private BigDecimal receivePresaleDiscountAmount;

    /**
     * 卖家退货商品的平台优惠劵折扣金额
     */
    private BigDecimal sellerPlatformCouponDiscountAmount;

    /**
     * 卖家退货商品的商家优惠劵金额
     */
    private BigDecimal sellerCouponDiscountAmount;

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
     * 平台支付促销优惠金额
     */
    private BigDecimal paymentPlatformDiscountAmount;

    /**
     * 商家支付促销优惠金额
     */
    private BigDecimal paymentShopDiscountAmount;

    /**
     * 配送商商品ID
     */
    private Long distributorGoodsId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品skuId
     */
    private Long goodsSkuId;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 商品ERP编码
     */
    private String goodsErpCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品批准文号
     */
    private String goodsLicenseNo;

    /**
     * 商品规格
     */
    private String goodsSpecification;

    /**
     * 商品生产厂家
     */
    private String goodsManufacturer;

    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;
    /**
     * 购买数量
     */
    private Integer goodsQuantity;

    /**
     * 商品小计
     */
    private BigDecimal goodsAmount;

    /**
     * 收货数量
     */
    private Integer receiveQuantity;

    /**
     * 收货金额
     */
    private BigDecimal receiveAmount;

    /**
     * 发货数量
     */
    private Integer deliveryQuantity;

    /**
     * 发货金额
     */
    private BigDecimal deliveryAmount;

    /**
     * 平台优惠劵平台承担比例
     */
    private BigDecimal platformRatio;
    /**
     * 商家券平台承担比例
     */
    private BigDecimal shopPlatformRatio;

    /**
     * 平台优惠劵商家承担比例
     */
    private BigDecimal platformBusinessRatio;

    /**
     * 商家券商家承担比例
     */
    private BigDecimal shopBusinessRatio;

    /**
     * 退货商品总小计
     */
    private BigDecimal returnAmount;

    /**
     * 客户ERP编码
     */
    private String customerErpCode;
}
