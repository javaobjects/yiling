package com.yiling.b2b.app.order.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *预售订单快速购买form
 */
@Data
public class PreSaleOrderQuickBuyForm {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(1L)
    @ApiModelProperty(value = "商品ID(以岭商品ID)", required = true)
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

    @ApiModelProperty(value = "预售活动Id", required = true)
    private Long  activityId;

}
