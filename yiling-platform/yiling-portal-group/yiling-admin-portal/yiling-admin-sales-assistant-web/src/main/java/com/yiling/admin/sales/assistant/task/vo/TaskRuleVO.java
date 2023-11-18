package com.yiling.admin.sales.assistant.task.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 任务规则 -参与条件
 * </p>
 *
 * @author gxl
 * @since 2020-04-21
 */
@Data
public class TaskRuleVO  {
    @ApiModelProperty(value = "规则id")
    private Integer id;

    @ApiModelProperty(value = "规则名称")
    private String ruleKey;

    @ApiModelProperty(value = "规则值")
    private String ruleValue;



}
