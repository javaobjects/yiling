package com.yiling.b2b.app.coupon.vo;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/12
 */
@Data
@Accessors(chain = true)
public class OrderUseCouponBudgetGoodsDetailVO {

    /**
     * 商品ID
     */
    @NotNull
    @ApiModelProperty(required = true, value = "商品ID")
    private Long goodsId;

    /**
     * 商品SKU ID
     */
    @NotNull
    @ApiModelProperty(required = true, value = "商品SKU ID")
    private Long goodsSkuId;

    /**
     * 商品SKU原始金额
     */
    @NotNull
    @ApiModelProperty(required = true, value = "商品SKU分摊优惠金额")
    private BigDecimal goodsSkuAmount;

    /**
     * 商品SKU分摊优惠分摊金额
     */
    @NotNull
    @ApiModelProperty(required = true, value = "商品SKU分摊优惠分摊金额")
    private BigDecimal shareAmount;

    /**
     * 商品SKU优惠后金额
     */
    @NotNull
    @ApiModelProperty(required = true, value = "商品SKU优惠后金额")
    private BigDecimal goodsSkuDiscountAmount;

}
