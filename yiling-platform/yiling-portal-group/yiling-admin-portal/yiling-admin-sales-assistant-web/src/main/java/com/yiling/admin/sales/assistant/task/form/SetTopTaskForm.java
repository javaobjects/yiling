package com.yiling.admin.sales.assistant.task.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: ray
 * @date: 2021/9/16
 */
@Data
public class SetTopTaskForm extends BaseForm {
    @NotNull
    @ApiModelProperty(value = "任务id")
    private Long id;
}