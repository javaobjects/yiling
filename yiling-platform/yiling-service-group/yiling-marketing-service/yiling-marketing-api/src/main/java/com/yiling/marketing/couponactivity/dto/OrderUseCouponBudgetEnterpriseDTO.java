package com.yiling.marketing.couponactivity.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/12
 */
@Data
@Accessors(chain = true)
public class OrderUseCouponBudgetEnterpriseDTO implements java.io.Serializable{

    private static final long serialVersionUID = -8949466325367349720L;

    /**
     * 企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 商家优惠券ID
     */
    private Long couponId;

    /**
     * 商家优惠券名称
     */
    private String couponName;

    /**
     * 商家优惠券活动ID
     */
    private Long couponActivityId;

    /**
     * 平台承担比例
     */
    private BigDecimal platformRatio;

    /**
     * 商家承担比例
     */
    private BigDecimal businessRatio;

    /**
     * 商家优惠券优惠金额
     */
    @NotNull
    private BigDecimal businessDiscountAmount;

    /**
     * 平台优惠券优惠金额
     */
    @NotNull
    private BigDecimal platformDiscountAmount;

    /**
     * 商品小计金额
     */
    @NotNull
    private BigDecimal totalAmount;

    /**
     * 商品SKU小计金额明细
     */
    @NotNull
    private List<OrderUseCouponBudgetGoodsDetailDTO> goodsSkuDetailList;

    /**
     * 此优惠券是否可叠加促销赠品
     */
    private Boolean isSuportPromotionGoods;

}
