package com.yiling.admin.data.center.enterprise.form;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改用户状态 Form
 *
 * @author xuan.zhou
 * @date 2022/7/4
 */
@Data
public class UpdateUserStatusForm {

    /**
     * 用户ID
     */
    @NotNull
    @ApiModelProperty(value = "用户ID", required = true)
    private Long id;

    /**
     * 状态：1-启用 2-停用
     */
    @NotNull
    @ApiModelProperty(value = "状态：1-启用 2-停用", required = true)
    private Integer status;
}
