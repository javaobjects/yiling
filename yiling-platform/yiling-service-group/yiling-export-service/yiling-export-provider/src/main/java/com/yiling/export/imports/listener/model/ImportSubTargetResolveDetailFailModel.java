package com.yiling.export.imports.listener.model;

import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yiling.export.excel.model.EasyExcelBaseFailModel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: GXL
 * @date: 2023-04-17
 */
@Getter
@Setter
@EqualsAndHashCode
public class ImportSubTargetResolveDetailFailModel extends EasyExcelBaseFailModel {
    @ExcelProperty(value = "分解编码",index = 0)
    private Long id;
    /**
     * 1月份目标
     */
    @ExcelProperty(value = "1月/元")
    private BigDecimal targetJan;

    /**
     * 2月份目标
     */
    @ExcelProperty(value = "2月/元")
    private BigDecimal targetFeb;

    /**
     * 3月份目标
     */
    @ExcelProperty(value = "3月/元")
    private BigDecimal targetMar;

    /**
     * 4月份目标
     */
    @ExcelProperty(value = "4月/元")
    private BigDecimal targetApr;

    /**
     * 5月份目标
     */
    @ExcelProperty(value = "5月/元")
    private BigDecimal targetMay;

    /**
     * 6月份目标
     */
    @ExcelProperty(value = "6月/元")
    private BigDecimal targetJun;

    /**
     * 7月份目标
     */
    @ExcelProperty(value = "7月/元")
    private BigDecimal targetJul;

    /**
     * 8月份目标
     */
    @ExcelProperty(value = "8月/元")
    private BigDecimal targetAug;

    /**
     * 9月份目标
     */
    @ExcelProperty(value = "9月/元")
    private BigDecimal targetSep;

    /**
     * 10月份目标
     */
    @ExcelProperty(value = "10月/元")
    private BigDecimal targetOct;

    /**
     * 11月份目标
     */
    @ExcelProperty(value = "11月/元")
    private BigDecimal targetNov;


    /**
     * 12月份目标
     */
    @ExcelProperty(value = "12月/元")
    private BigDecimal targetDec;

}
