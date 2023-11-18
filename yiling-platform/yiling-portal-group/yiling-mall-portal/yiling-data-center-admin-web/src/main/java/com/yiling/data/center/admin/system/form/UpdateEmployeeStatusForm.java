package com.yiling.data.center.admin.system.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改员工状态 Form
 *
 * @author: xuan.zhou
 * @date: 2021/7/26
 */
@Data
public class UpdateEmployeeStatusForm {

    @NotNull
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    @NotNull
    @ApiModelProperty(value = "状态：1-启用 2-停用", required = true)
    private Integer status;
}
