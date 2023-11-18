package com.yiling.data.center.admin.system.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建角色 Form
 *
 * @author xuan.zhou
 * @date 2021/7/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CreateRoleForm extends BaseForm {

	@NotEmpty
    @Length(max = 20)
	@ApiModelProperty(value = "角色名称", required = true)
	private String name;

    @Length(max = 100)
	@ApiModelProperty("角色描述")
	private String remark;

    @NotNull
    @Range(min = 1, max = 2)
    @ApiModelProperty(value = "状态：1-启用 2-停用", required = true)
    private Integer status;

    @ApiModelProperty(value = "子系统菜单及数据权限列表", required = true)
    private List<RolePermissionForm> rolePermissionList;

    @Valid
    @Data
    public static class RolePermissionForm {

        @NotNull
        @ApiModelProperty("应用ID：2-POP后台 3-B2B后台 4-互联网医院后台 5-数据中台 6-销售助手")
        private Integer appId;

        @ApiModelProperty("菜单ID集合")
        private List<Long> menuIds;

        @ApiModelProperty("数据范围：0-未定义 1-本人 2-本部门 3-本部门及下级部门 4-全部数据")
        private Integer dataScope;
    }
}
