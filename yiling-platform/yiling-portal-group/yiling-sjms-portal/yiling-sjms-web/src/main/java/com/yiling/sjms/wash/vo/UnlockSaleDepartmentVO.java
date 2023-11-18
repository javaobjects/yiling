package com.yiling.sjms.wash.vo;

import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
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
public class UnlockSaleDepartmentVO extends BaseVO {

    /**
     * 非锁销售规则主键
     */
    @ApiModelProperty("非锁销售规则主键")
    private Long ruleId;

    /**
     * 部门类型：1-crm部门2-自定义部门
     */
    @ApiModelProperty("部门类型：1-crm部门2-自定义部门")
    private Integer type;

    /**
     * crm部门编码
     */
    @ApiModelProperty("crm部门编码")
    private Long crmDepartmentCode;

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String departmentName;

    /**
     * 部门全名称
     */
    @ApiModelProperty("部门全名称")
    private String departmentFullName;

    /**
     * crm业务部门编码
     */
    @ApiModelProperty("crm业务部门编码")
    private Long crmBusinessDepartmentCode;

    /**
     * 业务部门名称
     */
    @ApiModelProperty("业务部门名称")
    private String businessDepartmentName;

    /**
     * 业务部门全名称
     */
    @ApiModelProperty("业务部门全名称")
    private String businessDepartmentFullName;

    @ApiModelProperty("销量计入省区、业务省区：1-商务负责人的省区、业务省区")
    private Integer  saleIncludedRange;

}
