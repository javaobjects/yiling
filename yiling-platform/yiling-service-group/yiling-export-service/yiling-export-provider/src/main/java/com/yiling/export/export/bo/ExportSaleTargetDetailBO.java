package com.yiling.export.export.bo;

import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
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
@Accessors(chain = true)
public class ExportSaleTargetDetailBO {



    /**
     * 指标编号
     */
    @Excel(name="指标编码")
    private String targetNo;
    /**
     * 指标名称
     */
    @Excel(name="指标名称")
    private String name;
    /**
     * 指标年份
     */
    @Excel(name="年份")
    private Long targetYear;


    /**
     * 部门名称
     */
    @Excel(name="部门")
    private String departName;

    /**
     * 标签为省区的部门名称
     */
    @Excel(name="省区")
    private String departProvinceName;



    /**
     * 标签为区办的部门名称
     */
    @Excel(name="区办")
    private String departRegionName;



    /**
     * 品类名称
     */
    @Excel(name="品种")
    private String categoryName;

    /**
     * 1月份目标
     */
    @Excel(name="1月/元")
    private BigDecimal targetJan;

    /**
     * 2月份目标
     */
    @Excel(name="2月/元")
    private BigDecimal targetFeb;

    /**
     * 3月份目标
     */
    @Excel(name="3月/元")
    private BigDecimal targetMar;

    /**
     * 4月份目标
     */
    @Excel(name="4月/元")
    private BigDecimal targetApr;

    /**
     * 5月份目标
     */
    @Excel(name="5月/元")
    private BigDecimal targetMay;

    /**
     * 6月份目标
     */
    @Excel(name="6月/元")
    private BigDecimal targetJun;

    /**
     * 7月份目标
     */
    @Excel(name="7月/元")
    private BigDecimal targetJul;

    /**
     * 8月份目标
     */
    @Excel(name="8月/元")
    private BigDecimal targetAug;

    /**
     * 9月份目标
     */
    @Excel(name="9月/元")
    private BigDecimal targetSep;

    /**
     * 10月份目标
     */
    @Excel(name="10月/元")
    private BigDecimal targetOct;

    /**
     * 11月份目标
     */
    @Excel(name="11月/元")
    private BigDecimal targetNov;

    /**
     * 12月份目标
     */
    @Excel(name="12月/元")
    private BigDecimal targetDec;


}
