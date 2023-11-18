package com.yiling.marketing.couponactivity.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.BaseDTO;

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
public class OrderUseCouponBudgetDTO extends BaseDTO {

    /**
     * 平台优惠券ID
     */
    private Long platformCouponId;

    /**
     * 平台优惠券名称
     */
    private String platformCouponName;

    /**
     * 平台优惠券活动ID
     */
    private Long platformCouponActivityId;

    /**
     * 平台承担比例
     */
    private BigDecimal platformRatio;

    /**
     * 商家承担比例
     */
    private BigDecimal businessRatio;

    /**
     * 商家优惠券抵扣金额
     */
    @NotNull
    private BigDecimal totalBusinessDiscountAmount;

    /**
     * 平台优惠券抵扣金额
     */
    @NotNull
    private BigDecimal totalPlatformDiscountAmount;

    /**
     * 企业信息明细
     */
    @NotNull
    private List<OrderUseCouponBudgetEnterpriseDTO> enterpriseGoodsList;

}
