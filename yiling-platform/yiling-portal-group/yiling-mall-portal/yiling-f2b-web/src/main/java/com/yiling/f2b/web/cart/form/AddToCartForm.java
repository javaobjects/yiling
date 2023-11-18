package com.yiling.f2b.web.cart.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 添加商品到购物车 Form
 *
 * @author: xuan.zhou
 * @date: 2021/6/18
 */
@Data
public class AddToCartForm {

    @NotNull
    @Min(1L)
    @ApiModelProperty(value = "商品ID", required = true)
    private Long goodsId;

    @NotNull
    @Min(1L)
    @ApiModelProperty(value = "商品SkuId", required = true)
    private Long goodsSkuId;

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "购买数量", required = true)
    private Integer quantity;

    @NotNull
    @Min(1L)
    @ApiModelProperty(value = "配送商企业ID", required = true)
    private Long distributorEid;

    @NotNull
    @Min(1L)
    @ApiModelProperty(value = "配送商商品ID", required = true)
    private Long distributorGoodsId;

}
