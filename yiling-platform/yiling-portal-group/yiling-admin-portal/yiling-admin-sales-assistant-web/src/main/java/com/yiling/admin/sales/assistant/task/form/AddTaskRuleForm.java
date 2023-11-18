package com.yiling.admin.sales.assistant.task.form;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建任务规则
 * @author: ray
 * @date: 2021/9/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddTaskRuleForm extends BaseForm {

    @ApiModelProperty(value = "规则类型 0:参与条件1：完成条件2：佣金设置")
    private Integer ruleType;

    @ApiModelProperty(value = "规则名称")
    private String ruleKey;

    @ApiModelProperty(value = "规则值")
    private String ruleValue;
}