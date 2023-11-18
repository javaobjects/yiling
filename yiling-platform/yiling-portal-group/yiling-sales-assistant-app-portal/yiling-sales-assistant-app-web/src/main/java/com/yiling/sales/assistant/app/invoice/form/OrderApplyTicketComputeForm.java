package com.yiling.sales.assistant.app.invoice.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 申请票折计算入参
 * @author:wei.wang
 * @date:2021/8/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderApplyTicketComputeForm extends BaseForm {
    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID",required = true)
    @NotNull
    private Long orderId;

    /**
     * 明细ID
     */
    @ApiModelProperty(value = "明细ID",required = true)
    @NotNull
    private Long detailId;

    /**
     * 票折方式
     */
    @ApiModelProperty(value = "票折方式：1-按比率 2-按金额",required = true)
    @NotNull
    private Integer invoiceDiscountType;

    /**
     * 金额值或者比率值
     */
    @ApiModelProperty(value = "金额值或者比率值",required = true)
    @NotNull
    private BigDecimal value;


}
