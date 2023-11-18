package com.yiling.order.order.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单详情
 * @author:wei.wang
 * @date:2021/6/18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderDetailDTO extends BaseDTO {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 商品标准库ID
     */
    private Long standardId;
    /**
     * 商品类型
     */
    private Integer goodsType;

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
     * 商品原价
     */
    private BigDecimal originalPrice;


    /**
     * 促销活动单价
     */
    private BigDecimal promotionGoodsPrice;

    /**
     * 限定价格
     */
    private BigDecimal limitPrice;

    /**
     * 购买数量
     */
    private Integer goodsQuantity;

    /**
     * 商品小计
     */
    private BigDecimal goodsAmount;

    /**
     * 预售定金金额
     */
    private BigDecimal depositAmount;

    /**
     * 现折金额
     */
    private BigDecimal cashDiscountAmount;

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
     *票折金额
     */
    private BigDecimal ticketDiscountAmount;


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
     * 退货商品的平台优惠劵折扣金额
     */
    private BigDecimal returnPlatformCouponDiscountAmount;

    /**
     * 退货商品的商家优惠劵折扣金额
     */
    private BigDecimal returnCouponDiscountAmount;

    /**
     * 收货商品的商家优惠劵金额
     */
    private BigDecimal receiveCouponDiscountAmount;

    /**
     * 收货平台的商家优惠劵金额
     */
    private BigDecimal receivePlatformCouponDiscountAmount;

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
     * 平台优惠劵折扣金额
     */
    private BigDecimal platformCouponDiscountAmount;

    /**
     * 商家优惠劵折扣金额
     */
    private BigDecimal couponDiscountAmount;



}
