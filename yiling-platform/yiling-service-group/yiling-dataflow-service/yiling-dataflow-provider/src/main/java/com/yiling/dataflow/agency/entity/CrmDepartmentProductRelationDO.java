package com.yiling.dataflow.agency.entity;

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
 * 业务部门与产品组对应关系表
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("crm_department_product_relation")
public class CrmDepartmentProductRelationDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * erp供应链角色：1-经销商 2-终端医院 3-终端药店。数据字典：erp_crm_supply_chain_role
     */
    private Integer supplyChainRole;

    /**
     * 部门id
     */
    private Long departmentId;

    /**
     * 部门
     */
    private String department;

    /**
     * 产品组
     */
    private String productGroup;

    /**
     * 产品组id
     */
    private Long productGroupId;

    /**
     * 导出模板对应名称
     */
    private String uploadFileName;

    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 更新日期
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private String remark;


}
