package com.yiling.sjms.agency.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 审核通过表单
 * @author: shixing.sun
 * @date: 2023/2/25
 */
@Data
public class EnterpriseApproveForm extends BaseForm {
    @ApiModelProperty("表单id")
    @NotNull
    private Long formId;

}