package com.yiling.user.esb.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * esb组织架构 DTO
 *
 * @author: xuan.zhou
 * @date: 2022/11/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class EsbOrganizationDTO extends BaseDTO {

    private static final long serialVersionUID = -6132994522900717901L;

    /**
     * 部门ID
     */
    private Long orgId;

    /**
     * 部门名称
     */
    private String orgName;

    /**
     * 组织类型：0-公司 1-部门
     */
    private String orgType;

    /**
     * 上级部门ID
     */
    private Long orgPid;

    /**
     * 所属公司
     */
    private Long compId;

    /**
     * 所在地（全部是石家庄）
     */
    private String orgArea;

    /**
     * HRID
     */
    private String sourceDetailId;

    /**
     * 是否失效：0-正常，其他失效
     */
    private String state;

    /**
     * 全路径
     */
    private String fullpath;

    /**
     * 组织状态：-1-停用 0-正常 1-保存未生效 2-删除
     */
    private String status;

    /**
     * 一级部门ID
     */
    private String plate;

    /**
     * 二级部门ID
     */
    private Long twoDeptId;

    /**
     * 二级部门名称
     */
    private String twoDeptName;

    /**
     * 三级部门ID
     */
    private Long deptId;

    /**
     * 三级部门名称
     */
    private String deptName;

    /**
     * 一级省区ID
     */
    private String provinceId;

    /**
     * 一级省区名称
     */
    private String provinceName;

    /**
     * 二级省区ID
     */
    private String bizProvinceId;

    /**
     * 二级省区名称
     */
    private String bizProvinceName;

    /**
     * 四级省区ID
     */
    private Long bizDeptId;

    /**
     * 四级省区名称
     */
    private String bizDeptName;

    /**
     * 财务负责人
     */
    private String financeManager;

    /**
     * 片区
     */
    private String area;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
