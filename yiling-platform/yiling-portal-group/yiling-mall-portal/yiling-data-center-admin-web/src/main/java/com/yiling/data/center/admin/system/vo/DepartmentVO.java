package com.yiling.data.center.admin.system.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业部门项 VO
 *
 * @author: yuecheng.chen
 * @date: 2021/6/21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class DepartmentVO extends BaseVO {

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String name;

    /**
     * 部门编码
     */
    @ApiModelProperty(value = "部门编码")
    private String code;

    /**
     * 部门负责人ID
     */
    @ApiModelProperty("部门负责人ID")
    private Long managerId;

    /**
     * 部门负责人姓名
     */
    @ApiModelProperty("部门负责人姓名")
    private String managerName;

    /**
     * 部门员工人数
     */
    @ApiModelProperty(value = "部门员工人数")
    private Long employeeNum;

    /**
     * 上级部门ID
     */
    @ApiModelProperty(value = "上级部门ID")
    private Long parentId;

    /**
     * 上级部门名称
     */
    @ApiModelProperty(value = "上级部门名称")
    private String parentName;

    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private String status;

    /**
     * 创建人ID
     */
    @ApiModelProperty("创建人ID")
    private Long createUser;

    /**
     * 创建人姓名
     */
    @ApiModelProperty("创建人姓名")
    private String createUserName;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

}
