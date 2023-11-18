package com.yiling.sjms.agency.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 提交机构扩展信息修改表单
 * @author: gxl
 * @date: 2023/2/25
 */
@Data
public class SubmitAgencyExtendChangeForm extends BaseForm {
    @ApiModelProperty("父表单id")
    @NotNull
    private Long formId;
}