package com.yiling.sjms.sale.form;

import javax.validation.constraints.NotNull;

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
public class QuerySaleDeptSubTargetDetailForm extends QueryPageListForm {
    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID",required = true)
    @NotNull
    private Long departId;

    /**
     * 指标ID
     */
    @ApiModelProperty(value = "指标ID",required = true)
    @NotNull
    private Long saleTargetId;

    @ApiModelProperty(value = "省区")
    private String departProvinceName;
    /**
     * 标签为区办的部门名称
     */
    @ApiModelProperty(value = "区办")
    private String departRegionName;
    /**
     * 品类名称
     */
    @ApiModelProperty(value = "品种")
    private String categoryName;
}