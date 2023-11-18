package com.yiling.user.enterprise.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 企业员工信息导出 DTO
 *
 * @author: lun.yu
 * @date: 2023-06-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EnterpriseEmployeeExportDTO extends BaseDTO {

    /**
     * 企业ID
     */
    private Long eid;

    /**
     * 员工用户ID
     */
    private Long userId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 工号
     */
    private String code;

    /**
     * 员工类型：1-商务代表 2-医药代表 100-其他
     */
    private Integer type;

    /**
     * 员工类型名称
     */
    private String typeName;

    /**
     * 上级领导ID
     */
    private Long parentId;

    /**
     * 上级领导名称
     */
    private String parentName;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 职位ID
     */
    private Long positionId;

    /**
     * 是否为企业管理员：0-否 1-是
     */
    private Integer adminFlag;

    /**
     * 状态：1-启用 2-停用
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建信息
     */
    private String createInfo;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

}
