package com.yiling.sales.assistant.app.paymentdays.form;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 申请临时额度 Form
 *
 * @author: lun.yu
 * @date: 2021/9/27
 */
@Data
@Accessors(chain = true)
public class ApplyQuotaForm extends BaseForm {

    /**
     * 企业ID（供应商ID）
     */
    @NotNull
    @ApiModelProperty(value = "企业ID（供应商ID）", required = true)
    private Long eid;

    /**
     * 客户ID（采购商ID）
     */
    @NotNull
    @ApiModelProperty(value = "客户ID（采购商ID）", required = true)
    private Long customerEid;

    /**
     * 临时额度
     */
    @DecimalMin(value = "0.01",message = "额度不能小于0.01")
    @ApiModelProperty(value = "临时额度", required = true)
    private BigDecimal temporaryAmount;

}
