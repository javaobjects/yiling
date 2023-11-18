package com.yiling.sjms.sale.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 销售指标部门配置
 * @author: gxl
 * @date: 2023/4/12
 */
@Data
public class SaleDepartmentTargetVO extends BaseVO {
    /**
     * 指标ID
     */
    @ApiModelProperty(value = "指标ID")
    private Long saleTargetId;

    /**
     * 指标名称
     */
    @ApiModelProperty(value = "指标名称")
    private String name;

    /**
     * 指标编号
     */
    @ApiModelProperty(value = "指标编号")
    private String targetNo;


    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID")
    private Long departId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称")
    private String departName;
    /**
     * 本年目标单位元
     */
    @ApiModelProperty(value = "本年目标单位元")
    private BigDecimal currentTarget;

    /**
     * 本年一年目标比例
     */
    @ApiModelProperty(value = "本年一年目标比例")
    private BigDecimal currentTargetRatio;

    /**
     * 状态 1-未配置 2-已配置 3-配置中
     */
    @ApiModelProperty(value = "状态 1-未配置 2-已配置 3-配置中")
    private Integer configStatus;
    /**
     * 目标配置时间
     */

    @ApiModelProperty(value = "目标配置时间")
    private Date configTime;

    /**
     * 目标分解状态 1未分解 2已分解
     */
    @ApiModelProperty(value = " 目标分解状态 1未分解 2已分解")
    private Integer resolveStatus;

    /**
     * 目标配置时间
     */
    @ApiModelProperty(value = "目标分解时间")
    private Date resolveTime;

    /**
     * 目标分解数
     */
    @ApiModelProperty(value = "目标分解数")
    private Integer goal;

    @ApiModelProperty(value = "指标年份")
    private Long targetYear;
    /**
     * 已分解数
     */
    @ApiModelProperty(value = "已分解数")
    private Integer resolved;

    @ApiModelProperty(value = "部门目标分解状态 提示")
    private Integer resolveToast;

    /**
     * 实际分解目标总和
     */
    @ApiModelProperty(value = "分解明细合计")
    private BigDecimal realTarget;
}