package com.yiling.b2b.app.cart.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 添加商品到购物车 Form
 *
 */
@Data
public class AddToCartForm {

    @ApiModelProperty(value = "商品ID(以岭商品ID)")
    private Long goodsId;

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "购买数量", required = true)
    private Integer quantity;

    @NotNull
    @Min(1L)
    @ApiModelProperty(value = "卖家企业ID", required = true)
    private Long distributorEid;

    @NotNull
    @Min(1L)
    @ApiModelProperty(value = "卖家商品ID(对应商家goodsId)", required = true)
    private Long distributorGoodsId;

    @NotNull
    @Min(1L)
    @ApiModelProperty(value = "规格sku(goodsSkuId)", required = true)
    private Long goodsSkuId;

    @ApiModelProperty(value = "促销活动类型：0正常：2特价,3秒杀", required = false)
    private Integer promotionActivityType;

}
