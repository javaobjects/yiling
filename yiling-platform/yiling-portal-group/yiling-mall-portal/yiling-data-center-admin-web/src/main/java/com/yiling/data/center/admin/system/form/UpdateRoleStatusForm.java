package com.yiling.data.center.admin.system.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 修改角色状态 Form
 *
 * @author: xuan.zhou
 * @date: 2021/7/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateRoleStatusForm extends BaseForm {

    @NotNull
    @ApiModelProperty(value = "角色ID", required = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "状态：1-启用 2-停用", required = true)
    private Integer status;
}
