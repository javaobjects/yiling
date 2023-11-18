package com.yiling.dataflow.sale.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: dexi.yao
 * @date: 2023-04-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaleDepartmentTargetDTO extends BaseDTO {


    private static final long serialVersionUID = -6929938666808674456L;

    /**
     * 指标ID
     */
    private Long saleTargetId;

    /**
     * 部门ID
     */
    private Long departId;

    /**
     * 部门名称
     */
    private String departName;

    /**
     * 上一年目标单位元
     */
    private BigDecimal lastTarget;

    /**
     * 上一年目标比例
     */
    private BigDecimal lastTargetRatio;

    /**
     * 本年目标单位元
     */
    private BigDecimal currentTarget;

    /**
     * 本年一年目标比例
     */
    private BigDecimal currentTargetRatio;

    /**
     * 本年度增加单位元
     */
    private BigDecimal currentIncrease;

    /**
     * 状态 1-未配置 2-已配置 3-配置中
     */
    private Integer configStatus;

    private String templateUrl;
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
     * 生成模板失败原因
     */
    private String generateTempErrMsg;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 更新人
     */
    private Long updateUser;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
