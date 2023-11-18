package com.yiling.sjms.sale.form;

import com.yiling.framework.common.base.form.QueryPageListForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 查询分解模板
 * @author: gxl
 * @date: 2023/4/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryResolveDetailPageForm extends QueryPageListForm {

    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门id")
    private Long departId;

    /**
     * 指标ID
     */
    @ApiModelProperty(value = "指标ID")
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
    /**
     * 月份目标值分解状态 1未分解（任意月份没填） 2已分解
     */
    @ApiModelProperty(value = "1未分解（任意月份没填） 2已分解")
    private Integer resolveStatus;
}