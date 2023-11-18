package com.yiling.dataflow.sale.bo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 部门销售指标子项配置详情
 * </p>
 *
 * @author gxl
 * @date 2023-04-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaleDepartmentSubTargetDetailBO extends BaseDTO {

    private static final long serialVersionUID = 7832836848271632815L;
    /**
     * 指标名称
     */
    private String name;

    /**
     * 指标编号
     */
    private String targetNo;
    /**
     * 指标年份
     */
    private Long targetYear;
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
     * 标签为省区的部门ID
     */
    private Long departProvinceId;

    /**
     * 标签为省区的部门名称
     */
    private String departProvinceName;

    /**
     * 标签为区办的部门ID
     */
    private Long departRegionId;

    /**
     * 标签为区办的部门名称
     */
    private String departRegionName;

    /**
     * 品类ID
     */
    private Long categoryId;

    /**
     * 品类名称
     */
    private String categoryName;

    /**
     * 1月份目标
     */
    private BigDecimal targetJan;

    /**
     * 2月份目标
     */
    private BigDecimal targetFeb;

    /**
     * 3月份目标
     */
    private BigDecimal targetMar;

    /**
     * 4月份目标
     */
    private BigDecimal targetApr;

    /**
     * 5月份目标
     */
    private BigDecimal targetMay;

    /**
     * 6月份目标
     */
    private BigDecimal targetJun;

    /**
     * 7月份目标
     */
    private BigDecimal targetJul;

    /**
     * 8月份目标
     */
    private BigDecimal targetAug;

    /**
     * 9月份目标
     */
    private BigDecimal targetSep;

    /**
     * 10月份目标
     */
    private BigDecimal targetOct;

    /**
     * 11月份目标
     */
    private BigDecimal targetNov;

    /**
     * 12月份目标
     */
    private BigDecimal targetDec;

    /**
     * 备注
     */
    private String remark;

    /**
     * 本年度增加单位元
     */
    private BigDecimal currentIncrease;


}
