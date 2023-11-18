package com.yiling.dataflow.wash.dto.request;

import com.yiling.framework.common.base.form.BaseForm;
import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/5/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveUnlockSaleDepartmentRequest extends BaseRequest {

    private Long id;

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
