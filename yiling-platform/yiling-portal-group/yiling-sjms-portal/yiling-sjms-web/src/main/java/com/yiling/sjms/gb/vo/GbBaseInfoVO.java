package com.yiling.sjms.gb.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 团购基础信息
 */
@Data
public class GbBaseInfoVO {

    /**
     * 省区名称
     */
    @ApiModelProperty(value = "省区名称")
    private String provinceName;

    /**
     * 事业部ID
     */
    @ApiModelProperty(value = "事业部ID")
    private Long orgId;

    /**
     * 事业部名称
     */
    @ApiModelProperty(value = "事业部名称")
    private String orgName;

    /**
     * 销量计入人工号
     */
    @ApiModelProperty(value = "销量计入人工号")
    private String sellerEmpId;

    /**
     * 销量计入人姓名
     */
    @ApiModelProperty(value = "销量计入人姓名")
    private String sellerEmpName;

    /**
     * 销量计入人区办ID
     */
    @ApiModelProperty(value = "销量计入人区办ID")
    private Long sellerDeptId;

    /**
     * 销量计入人区办名称
     */
    @ApiModelProperty(value = "销量计入人区办名称")
    private String sellerDeptName;

    /**
     * 销量计入人部门名称
     */
    @ApiModelProperty(value = "销量计入人部门名称")
    private String sellerYxDeptName;

    /**
     * 团购负责人工号
     */
    @ApiModelProperty(value = "团购负责人工号")
    private String managerEmpId;

    /**
     * 团购负责人姓名
     */
    @ApiModelProperty(value = "团购负责人姓名")
    private String managerEmpName;

    /**
     * 团购负责人部门名称
     */
    @ApiModelProperty(value = "团购负责人部门名称")
    private String managerYxDeptName;
}
