package com.yiling.dataflow.sale.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 销售指标部门配置
 * @author: gxl
 * @date: 2023/4/12
 */
@Data
public class SaleDepartmentTargetBO implements Serializable {
    private static final long serialVersionUID = -5598147110321672212L;
    /**
     * 指标ID
     */
    private Long saleTargetId;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 指标编号
     */
    private String targetNo;


    /**
     * 部门ID
     */
    private Long departId;

    /**
     * 部门名称
     */
    private String departName;
    /**
     * 本年目标单位元
     */
    private BigDecimal currentTarget;

    /**
     * 本年一年目标比例
     */
    private BigDecimal currentTargetRatio;

    /**
     * 状态 1-未配置 2-已配置 3-配置中
     */
    private Integer configStatus;
    /**
     * 目标配置时间
     */
    private Date configTime;

    /**
     * 目标分解状态 1未分解 2已分解
     */
    private Integer resolveStatus;

    /**
     * 目标配置时间
     */
    private Date resolveTime;

    /**
     * 目标拆解数
     */
    private Integer goal;

    /**
     * 已拆解数
     */
    private Integer resolved;

    private Long targetYear;

    /**
     * 部门目标分解状态 提示
     */
    private Integer resolveToast;

    /**
     * 实际分解目标总和
     */
    private BigDecimal realTarget;
}