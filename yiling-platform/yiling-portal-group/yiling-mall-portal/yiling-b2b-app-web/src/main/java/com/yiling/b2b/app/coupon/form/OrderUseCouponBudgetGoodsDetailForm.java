package com.yiling.b2b.app.coupon.form;

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
public class OrderUseCouponBudgetGoodsDetailForm {

    @NotNull
    @ApiModelProperty(required = true, value = "商品ID")
    private Long goodsId;

    @NotNull
    @ApiModelProperty(required = true, value = "商品SKU ID")
    private Long goodsSkuId;

    @NotNull
    @ApiModelProperty(required = true, value = "商品SKU小计金额")
    private BigDecimal goodsSkuAmount;

}
