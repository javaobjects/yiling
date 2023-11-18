package com.yiling.dataflow.wash.dto;

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
 * 
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UnlockSaleDepartmentDTO extends BaseDTO {

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

}
