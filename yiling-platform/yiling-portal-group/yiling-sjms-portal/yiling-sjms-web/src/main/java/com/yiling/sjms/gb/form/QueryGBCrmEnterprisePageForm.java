package com.yiling.sjms.gb.form;


import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;
import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryGBCrmEnterprisePageForm extends QueryPageListForm {

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String likeName;

    /**
     * 企业类型EnterpriseTypeNameEnum.name
     */
    @NotNull
    @ApiModelProperty(value = "状态 0-出库终端，1-出库商业")
    private Integer type;
}
