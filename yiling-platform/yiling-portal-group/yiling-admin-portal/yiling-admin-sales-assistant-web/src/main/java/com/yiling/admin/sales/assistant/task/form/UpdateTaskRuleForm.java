package com.yiling.admin.sales.assistant.task.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/14
 */
@Data
@Accessors(chain = true)
public class UpdateTaskRuleForm {
    @ApiModelProperty(value = "规则值")
    private String ruleValue;
    @ApiModelProperty(value = "规则id")
    @NotNull
    private Integer taskRuleId;
}