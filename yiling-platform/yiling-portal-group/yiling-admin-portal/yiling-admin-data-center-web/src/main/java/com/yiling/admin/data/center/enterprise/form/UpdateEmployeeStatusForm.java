package com.yiling.admin.data.center.enterprise.form;

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

    /**
     * 企业ID
     */
    @NotNull
    @ApiModelProperty(value = "企业ID", required = true)
    private Long eid;

    /**
     * 用户ID
     */
    @NotNull
    @ApiModelProperty(value = "用户ID", required = true)
    private Long userId;

    /**
     * 状态：1-启用 2-停用
     */
    @NotNull
    @ApiModelProperty(value = "状态：1-启用 2-停用", required = true)
    private Integer status;
}
