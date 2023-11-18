package com.yiling.admin.sales.assistant.task.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/14
 */
@Data
@Accessors(chain = true)
public class UpdateTaskDeptUserForm {
    @ApiModelProperty(value = "部门人员id", required = true)
    private Long userId;

    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id", required = true)
    private Long deptId;

    /**
     * 任务id
     */
    @ApiModelProperty(value = "任务id", required = true)
    private Long taskId;
}