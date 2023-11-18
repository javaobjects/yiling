package com.yiling.b2b.app.coupon.vo;

import java.math.BigDecimal;
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
public class OrderUseCouponBudgetEnterpriseVO {

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
     * 优惠券优惠金额
     */
    @NotNull
    @ApiModelProperty(required = true, value = "优惠券优惠金额")
    private BigDecimal discountAmount;

    /**
     * 小计金额
     */
    @NotNull
    @ApiModelProperty(required = true, value = "小计金额")
    private BigDecimal totalAmount;

    /**
     * 商品SKU小计金额明细
     */
    @NotNull
    @ApiModelProperty(required = true, value = "优惠券ID")
    private List<OrderUseCouponBudgetGoodsDetailVO> goodsSkuDetailList;

}
