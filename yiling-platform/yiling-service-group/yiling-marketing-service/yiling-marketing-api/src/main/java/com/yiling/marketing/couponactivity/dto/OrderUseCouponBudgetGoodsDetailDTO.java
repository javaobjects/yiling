package com.yiling.marketing.couponactivity.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/12
 */
@Data
@Accessors(chain = true)
public class OrderUseCouponBudgetGoodsDetailDTO implements java.io.Serializable{

    private static final long serialVersionUID = -3326657841649476441L;

    /**
     * 商品ID
     */
    @NotNull
    private Long goodsId;

    /**
     * 商品SKU ID
     */
    @NotNull
    private Long goodsSkuId;

    /**
     * 商品SKU原始金额
     */
    @NotNull
    private BigDecimal goodsSkuAmount;

    /**
     * 商品SKU分摊优惠分摊金额
     */
    @NotNull
    private BigDecimal businessShareAmount;

    /**
     * 商品SKU分摊优惠分摊金额
     */
    @NotNull
    private BigDecimal platformShareAmount;

    /**
     * 商品SKU优惠后金额
     */
    @NotNull
    private BigDecimal goodsSkuDiscountAmount;

}
