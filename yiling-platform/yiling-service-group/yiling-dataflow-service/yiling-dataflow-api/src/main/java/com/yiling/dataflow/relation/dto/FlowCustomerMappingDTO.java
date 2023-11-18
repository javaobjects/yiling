package com.yiling.dataflow.relation.dto;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2022/7/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowCustomerMappingDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 流向过来的客户名称
     */
    private String flowCustomerName;

    /**
     * CRM内码+CRM类型码
     */
    private String innerCode;

    /**
     * CRM 中标准名称
     */
    private String innerName;

    /**
     * 商业公司CRM内码
     */
    private String businessCode;

    /**
     * 商业公司名称（流向来源）
     */
    private String fromBusiness;

    private String businessTypeName;

    /**
     * 类型名称
     */
    private String customerTypeName;

    /**
     * 归属部门
     */
    private String belongDepartment;

    /**
     * 锁定类型：0 非锁 ， 1 锁定
     */
    private Integer lockType;

}
