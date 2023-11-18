package com.yiling.sjms.monthflow.form;


import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: gxl
 * @date: 2023/6/25
 */
@Data
public class SubmitMonthFlowForm extends BaseForm {

    @ApiModelProperty("父表单id")
    @NotNull(message = "提交表单信息不完整")
    private Long formId;

    /**
     * 申诉类型 1补传月流向
     */
    @ApiModelProperty(value = "申诉类型 1补传月流向 字典apply_appeal_type")
    private Integer appealType;

    /**
     * 申诉金额
     */
    @ApiModelProperty("申诉金额")
    private BigDecimal appealAmount;

    /**
     * 申诉描述
     */
    @ApiModelProperty(value = "申诉描述")
    private String appealDescribe;

    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    private List<AppendFile> appendixList;

}
