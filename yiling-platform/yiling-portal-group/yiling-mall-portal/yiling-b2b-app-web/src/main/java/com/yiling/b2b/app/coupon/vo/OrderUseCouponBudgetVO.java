package com.yiling.b2b.app.coupon.vo;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2021/11/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderUseCouponBudgetVO  extends BaseVO {

    /**
     * 平台优惠券ID
     */
    @NotNull
    @ApiModelProperty(required = true, value = "平台优惠券ID")
    private Long platformCouponId;

    /**
     * 商家优惠券抵扣金额
     */
    @NotNull
    @ApiModelProperty(required = true, value = "商家优惠券抵扣金额")
    private BigDecimal businessDiscountAmount;

    /**
     * 平台优惠券抵扣金额
     */
    @NotNull
    @ApiModelProperty(required = true, value = "平台优惠券抵扣金额")
    private BigDecimal platformDiscountAmount;

    /**
     * 企业信息明细
     */
    @NotNull
    @ApiModelProperty(required = true, value = "企业信息明细")
    private List<OrderUseCouponBudgetEnterpriseVO> enterpriseGoodsList;

}
