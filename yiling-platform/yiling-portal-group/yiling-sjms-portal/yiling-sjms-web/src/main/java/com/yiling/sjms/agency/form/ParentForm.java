package com.yiling.sjms.agency.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 父表单
 * @author: gxl
 * @date: 2023/2/25
 */
@Data
public class ParentForm {
    /**
     * form表主键
     */
    @ApiModelProperty(value = "主表单id",required = false)
    private Long formId;

    @ApiModelProperty(value = "调整原因",required = false)
    @Length(max = 500)
    private String adjustReason;

    @ApiModelProperty(value = "表单类型 2-机构扩展信息修改", required = true)
    @NotNull
    private Integer formType;
}