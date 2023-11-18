package com.yiling.b2b.admin.paymentdays.form;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 创建账期账户 Form
 *
 * @author: lun.yu
 * @date: 2021/10/28
 */
@Data
public class CreatePaymentDaysForm extends BaseForm {

    /**
     * 采购商ID
     */
    @NotNull
    @ApiModelProperty(value = "采购商ID", required = true)
    private Long customerEid;

    /**
     * 账期额度
     */
    @NotNull
    @DecimalMin(value = "0.01",message = "额度不能小于0.01")
    @ApiModelProperty(value = "账期额度", required = true)
    private BigDecimal totalAmount;

    /**
     * 还款周期
     */
    @Range(min = 1)
    @NotNull
    @ApiModelProperty(value = "还款周期", required = true)
    private Integer period;

    /**
     * 状态：1-启用 2-停用
     */
    @Range(min = 1,max = 2)
    @NotNull
    @ApiModelProperty(value = "状态：1-启用 2-停用", required = true)
    private Integer status;

    /**
     * 账期上浮点位（百分比）
     */
    @DecimalMin(value = "0.01",message = "额度不能小于0.01")
    @ApiModelProperty(value = "账期上浮点位（百分比）")
    private BigDecimal upPoint;

}
