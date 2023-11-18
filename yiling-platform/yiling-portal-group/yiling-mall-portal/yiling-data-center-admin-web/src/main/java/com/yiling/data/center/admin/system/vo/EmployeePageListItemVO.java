package com.yiling.data.center.admin.system.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 员工分页列表项 VO
 *
 * @author: xuan.zhou
 * @date: 2021/5/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EmployeePageListItemVO extends BaseVO {

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("工号")
    private String code;

    @ApiModelProperty("员工类型（数据字典：employee_type）")
    private Integer type;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("上级领导名称")
    private String parentName;

    @ApiModelProperty("状态：1-启用 2-停用")
    private Integer status;

    @ApiModelProperty("创建人ID")
    private Long createUser;

    @ApiModelProperty("创建人姓名")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("是否为企业管理员：0-否 1-是")
    private Integer adminFlag;

}
