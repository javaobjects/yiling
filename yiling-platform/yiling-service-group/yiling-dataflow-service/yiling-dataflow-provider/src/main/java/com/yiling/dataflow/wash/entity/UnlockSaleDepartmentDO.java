package com.yiling.dataflow.wash.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("unlock_sale_department")
public class UnlockSaleDepartmentDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 非锁销售规则主键
     */
    private Long ruleId;

    /**
     * 部门类型：1-crm部门2-自定义部门
     */
    private Integer type;

    /**
     * crm部门编码
     */
    private Long crmDepartmentCode;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 部门全名称
     */
    private String departmentFullName;

    /**
     * crm业务部门编码
     */
    private Long crmBusinessDepartmentCode;

    /**
     * 业务部门名称
     */
    private String businessDepartmentName;

    /**
     * 业务部门全名称
     */
    private String businessDepartmentFullName;

    private Integer  saleIncludedRange;


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
