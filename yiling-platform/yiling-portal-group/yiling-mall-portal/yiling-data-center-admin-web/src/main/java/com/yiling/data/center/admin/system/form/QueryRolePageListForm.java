package com.yiling.data.center.admin.system.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询角色分页列表 Form
 *
 * @author dexi.yao
 * @date 2021-06-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryRolePageListForm extends QueryPageListForm {

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String name;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：0-全部 1-启用 2-停用")
    private Integer status;
}
