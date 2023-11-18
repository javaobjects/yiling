package com.yiling.admin.b2b.paypromotion.from;

import java.math.BigDecimal;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shixing.sun
 * @date: 2022/9/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PayPromotionCaculateRuleFrom extends BaseForm {

    /**
     * 支付促销活动id
     */
    @ApiModelProperty("支付促销活动id")
    private Long id;

    /**
     * 支付促销活动id
     */
    @ApiModelProperty("支付促销活动id")
    private Long marketingPayId;

    /**
     * 促销规则类型（1-满减券；2-满折券）
     */
    @ApiModelProperty("促销规则类型（1-满减券；2-满折券）")
    private Integer type;

    /**
     * 门槛金额/件数
     */
    @ApiModelProperty("门槛金额/件数")
    private BigDecimal thresholdValue;

    /**
     * 优惠内容（金额/折扣比例）
     */
    @ApiModelProperty("优惠内容（金额/折扣比例）")
    private BigDecimal discountValue;

    /**
     * 最高优惠金额,满折时候使用
     */
    @ApiModelProperty("活动名称")
    private BigDecimal discountMax;
}
