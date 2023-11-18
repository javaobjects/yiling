package com.yiling.data.center.admin.system.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 员工信息
 *
 * @author: xuan.zhou
 * @date: 2021/7/5
 */
@Data
public class EmployeeVO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("工号")
    private String code;

    @ApiModelProperty("员工类型（数据字典employee_type）")
    private Integer type;

    @ApiModelProperty("职位ID")
    private Long positionId;

    @ApiModelProperty("职位名称")
    private String positionName;

    @ApiModelProperty("上级领导ID")
    private Long parentId;

    @ApiModelProperty("上级领导名称")
    private String parentName;

    @ApiModelProperty("所属部门列表")
    private List<SimpleDepartmentVO> departmentList;

}
