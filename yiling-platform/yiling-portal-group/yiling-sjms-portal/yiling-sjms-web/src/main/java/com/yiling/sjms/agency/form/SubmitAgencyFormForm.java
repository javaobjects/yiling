package com.yiling.sjms.agency.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2023/2/25 0025
 */
@Data
public class SubmitAgencyFormForm extends BaseForm {

    @ApiModelProperty("父表单id")
    @NotNull(message = "提交表单信息不完整")
    private Long formId;
}
