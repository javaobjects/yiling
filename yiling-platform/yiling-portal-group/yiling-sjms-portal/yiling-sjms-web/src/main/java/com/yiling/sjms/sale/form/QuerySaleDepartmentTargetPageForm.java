package com.yiling.sjms.sale.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: gxl
 * @date: 2023/4/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QuerySaleDepartmentTargetPageForm extends QueryPageListForm {
    /**
     * 指标编号
     */
    @ApiModelProperty(value = "指标编号")
    private String targetNo;
    @ApiModelProperty(value = "指标名称")
    private String name;



    @ApiModelProperty(value = "部门名称")
    private String departName;
    /**
      * 状态 1-未配置 2-已配置 3-配置中
     */
    @ApiModelProperty(value = "状态 1-未配置 2-已配置 3-配置中")
    private Integer configStatus;
}