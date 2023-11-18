package com.yiling.admin.sales.assistant.task.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:gxl
 * @description:
 * @date: Created in 19:18 2020/5/8
 * @modified By:
 */
@Data
public class StopTaskForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "任务id")
    private Long id;

}
