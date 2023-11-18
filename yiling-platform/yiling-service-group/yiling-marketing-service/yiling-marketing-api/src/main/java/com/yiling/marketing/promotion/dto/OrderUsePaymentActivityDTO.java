package com.yiling.marketing.promotion.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderUsePaymentActivityDTO extends BaseDTO {

    /**
     * 平台优惠名称
     */
    private String activityName;

    /**
     * 平台支付促销活动Id
     */
    private Long platformPaymentActivityId;

    /**
     * 支付促销计算规则id
     */
    private Long platformRuleId;

    /**
     * 费用承担方（1-平台；2-商家）
     */
    private Integer bear;

    /**
     * 平台承担比例
     */
    private BigDecimal platformRatio;

    /**
     * 商家承担比例
     */
    private BigDecimal businessRatio;

    /**
     * 商家优惠抵扣金额
     */
    @NotNull
    private BigDecimal totalBusinessDiscountAmount;

    /**
     * 平台优惠抵扣金额
     */
    @NotNull
    private BigDecimal totalPlatformDiscountAmount;

    /**
     * 企业信息明细
     */
    @NotNull
    private List<OrderUsePaymentActivityEnterpriseDTO> enterpriseGoodsList;

}
