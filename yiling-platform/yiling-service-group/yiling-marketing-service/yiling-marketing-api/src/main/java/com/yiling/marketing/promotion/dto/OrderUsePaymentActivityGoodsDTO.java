package com.yiling.marketing.promotion.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderUsePaymentActivityGoodsDTO implements java.io.Serializable {

    private static final long serialVersionUID = -3326657841649476441L;

    /**
     * 店铺企业ID
     */
    @NotNull
    private Long eid;

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
     * 分摊唯一商品标识Id
     */
    @NotNull
    private String relationId;

    /**
     * 商品原始金额
     */
    @NotNull
    private BigDecimal goodsAmount;

    /**
     * 商品SKU分摊优惠分摊金额
     */
    @NotNull
    private BigDecimal businessShareAmount;

    /**
     * 商品SKU分摊平台优惠分摊金额
     */
    @NotNull
    private BigDecimal platformShareAmount;

}
