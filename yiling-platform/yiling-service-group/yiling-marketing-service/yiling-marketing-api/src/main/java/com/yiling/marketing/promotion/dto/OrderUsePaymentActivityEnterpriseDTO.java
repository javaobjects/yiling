package com.yiling.marketing.promotion.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderUsePaymentActivityEnterpriseDTO implements java.io.Serializable {

    private static final long serialVersionUID = -8949466325367349720L;

    /**
     * 企业ID
     */
    @NotNull
    private Long eid;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 费用承担方（1-平台；2-商家）
     */
    private Integer bear;

    /**
     * 商家活动ID
     */
    private Long shopActivityId;

    /**
     * 支付促销计算规则id
     */
    private Long shopRuleId;

    /**
     * 平台承担比例
     */
    private BigDecimal platformRatio;

    /**
     * 商家承担比例
     */
    private BigDecimal businessRatio;

    /**
     * 商家优惠优惠金额
     */
    @NotNull
    private BigDecimal businessDiscountAmount;

    /**
     * 平台优惠优惠金额
     */
    @NotNull
    private BigDecimal platformDiscountAmount;

    /**
     * 商品小计金额
     */
    @NotNull
    private BigDecimal totalAmount;

    /**
     * 商品小计金额
     */
    @NotNull
    private List<OrderUsePaymentActivityGoodsDTO> goodsDetailList;

}
