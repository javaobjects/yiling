package com.yiling.b2b.app.coupon.form;

import java.util.List;

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
public class OrderUseCouponBudgetEnterpriseForm {

    /**
     * 企业ID
     */
    @ApiModelProperty(required = true, value = "企业ID")
    @NotNull
    private Long eid;

    /**
     * 优惠券ID
     */
    @NotNull
    @ApiModelProperty(required = true, value = "优惠券ID")
    private Long couponId;

    /**
     * 商品SKU小计金额明细
     */
    @NotNull
    @ApiModelProperty(required = true, value = "优惠券ID")
    private List<OrderUseCouponBudgetGoodsDetailForm> goodsSkuDetailList;


}
