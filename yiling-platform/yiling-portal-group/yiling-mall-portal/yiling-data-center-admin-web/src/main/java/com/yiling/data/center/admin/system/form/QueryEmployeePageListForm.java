package com.yiling.data.center.admin.system.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询员工分页列表 Form
 *
 * @author: xuan.zhou
 * @date: 2021/5/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryEmployeePageListForm extends QueryPageListForm {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 工号
     */
    @ApiModelProperty("工号")
    private String code;

    /**
     * 员工类型（数据字典：employee_type）
     */
    @ApiModelProperty("员工类型（数据字典：employee_type）")
    private Integer type;

    /**
     * 部门ID
     */
    @ApiModelProperty("部门ID")
    private Long departmentId;

    /**
     * 职位ID
     */
    @ApiModelProperty("职位ID")
    private Long positionId;

    /**
     * 状态：0-全部 1-启用 2-停用
     */
    @ApiModelProperty("状态：0-全部 1-启用 2-停用")
    private Integer status;
}
