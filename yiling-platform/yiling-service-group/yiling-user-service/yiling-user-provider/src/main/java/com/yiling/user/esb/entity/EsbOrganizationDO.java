package com.yiling.user.esb.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * esb组织架构
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-11-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("esb_organization")
public class EsbOrganizationDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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
    @TableField("`state`")
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
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
