package com.yiling.data.center.admin.system.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 转移部门员工 Form
 *
 * @author: xuan.zhou
 * @date: 2021/11/3
 */
@Data
public class MoveDepartmentEmployeeForm {

    @NotNull
    @ApiModelProperty(value = "来源部门ID", required = true)
    private Long sourceId;

    @NotNull
    @ApiModelProperty(value = "目标部门ID", required = true)
    private Long targetId;
}
