package com.yiling.sjms.agency.form;

import com.yiling.framework.common.base.form.BaseForm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class QueryAgencyInfoForm extends BaseForm {
    @ApiModelProperty("机构名称")
    @NotBlank
    private String name;
}
