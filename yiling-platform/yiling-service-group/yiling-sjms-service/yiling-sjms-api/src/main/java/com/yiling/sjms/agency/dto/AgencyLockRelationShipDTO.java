package com.yiling.sjms.agency.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 机构锁定三者关系表
 * </p>
 *
 * @author dexi.yao
 * @date 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AgencyLockRelationShipDTO extends BaseDTO {

    private static final long serialVersionUID = -6156626951365118603L;

    /**
     * 机构锁定信息表
     */
    private Long agencyFormId;

    /**f
     * 机构基本信息id
     */
    private Long crmEnterpriseId;

    /**
     * 岗位代码
     */
    private Long postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 产品组
     */
    private String productGroup;

    /**
     * 代表编码
     */
    private String representativeCode;

    /**
     * 代表名称
     */
    private String representativeName;

    /**
     * 客户编码
     */
    private String customerCode;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 供应链角色
     */
    private String supplyChainRole;

    /**
     * 部门
     */
    private String department;

    /**
     * 业务部门
     */
    private String businessDepartment;

    /**
     * 省区
     */
    private String provincialArea;

    /**
     * 业务省区
     */
    private String businessProvince;

    /**
     * 业务区域代码
     */
    private String businessAreaCode;

    /**
     * 业务区域
     */
    private String businessArea;

    /**
     * 上级主管编码
     */
    private String superiorSupervisorCode;

    /**
     * 上级主管名称
     */
    private String superiorSupervisorName;

    /**
     * 1-新增,2-修改
     */
    private Integer optType;

    /**
     * 修改三者关系表ID
     */
    private Long srcRelationShipIp;

    /**
     * 产品组id
     */
    private Long productGroupId;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 更新日期
     */
    private Date updateTime;

    private String remark;


}
