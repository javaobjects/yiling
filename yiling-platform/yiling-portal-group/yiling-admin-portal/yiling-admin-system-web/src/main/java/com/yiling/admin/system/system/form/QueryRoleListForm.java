package com.yiling.admin.system.system.form;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询角色列表 Form
 *
 * @author lun.yu
 * @date 2021-07-26
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel
public class QueryRoleListForm extends BaseForm {

    /**
     * 应用ID（参考PermissionAppEnum）
     */
    @NotNull(message = "应用ID不能为空")
    @ApiModelProperty(value = "应用ID")
    private Integer appId;

    /**
     * 企业ID
     */
    @ApiModelProperty(value = "企业ID")
    private Integer eid;

    /**
     * 角色名
     */
    @ApiModelProperty(value = "角色名")
    private String name;

    /**
     * 角色类型：1-系统角色 2-自定义角色
     */
    @ApiModelProperty(value = "角色类型：1-系统角色 2-自定义角色")
    private Integer type;

    /**
     * 状态： 1-启用 2-停用
     */
    @ApiModelProperty(value = "状态: 1-启用 2-停用")
    private Integer status;
}
