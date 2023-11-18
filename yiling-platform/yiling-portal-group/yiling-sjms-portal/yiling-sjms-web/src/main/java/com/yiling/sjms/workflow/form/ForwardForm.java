package com.yiling.sjms.workflow.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 流程转发 Form
 *
 * @author: xuan.zhou
 * @date: 2023/2/15
 */
@Data
public class ForwardForm {

    @NotNull
    @ApiModelProperty(value = "单据ID", required = true)
    private Long formId;

    @NotEmpty
    @ApiModelProperty(value = "转发员工工号列表", required = true)
    private List<String> toEmpIds;

    @ApiModelProperty(value = "转发提示语")
    private String text;
}
