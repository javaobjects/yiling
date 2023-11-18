package com.yiling.marketing.couponactivity.dto.request;

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
public class OrderUseCouponBudgetGoodsDetailRequest implements java.io.Serializable {
    private static final long serialVersionUID = -449266048434826477L;

    /**
     * 店铺企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 支付方式(1-线下支付 2-账期 3-预付款 4-在线支付)
     */
    private Integer payMethod;

    /**
     * 商家优惠券ID
     */
    private Long couponId;

    /**
     * 平台优惠券ID
     */
    private Long platformCouponId;

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
     * 商品SKU小计金额
     */
    @NotNull
    private BigDecimal goodsSkuAmount;


}
