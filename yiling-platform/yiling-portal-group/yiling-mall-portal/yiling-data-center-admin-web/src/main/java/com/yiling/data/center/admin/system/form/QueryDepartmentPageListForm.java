package com.yiling.data.center.admin.system.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询企业部门分页列表信息 Form
 * </p>
 *
 * @author yuecheng.chen
 * @date 2021-06-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryDepartmentPageListForm extends QueryPageListForm {

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String name;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：0-全部 1-启用 2-停用")
    private Integer status;

}
