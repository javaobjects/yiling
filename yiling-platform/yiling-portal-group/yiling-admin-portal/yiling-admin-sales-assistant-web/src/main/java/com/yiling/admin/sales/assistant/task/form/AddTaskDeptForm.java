package com.yiling.admin.sales.assistant.task.form;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: gxl
 * @date: 2023/1/9
 */
@Data
public class AddTaskDeptForm {
    @ApiModelProperty(value = "部门id")
    private Long id;
    @ApiModelProperty(value = "子部门id")
    private List<Long> children;
}