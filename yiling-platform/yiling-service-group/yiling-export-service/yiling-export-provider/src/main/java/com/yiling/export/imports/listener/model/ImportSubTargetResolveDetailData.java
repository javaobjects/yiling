package com.yiling.export.imports.listener.model;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yiling.export.excel.model.EasyExcelBaseModel;

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
public class ImportSubTargetResolveDetailData extends EasyExcelBaseModel {
    @NotNull(message = "分解编码不能为空")
    @ExcelProperty(value = "分解编码")
    private Long id;
    /**
     * 1月份目标
     */
    //@NumberFormat("#.##")
    @Digits(integer = 100000000, fraction = 2,message = "数据格式错误")
    @ExcelProperty(value = "1月/元")
    @NotNull(message = "数据内容为空")
    @Min(value = 0,message = "数据格式错误")
    private BigDecimal targetJan;

    /**
     * 2月份目标
     */
    @Digits(integer = 100000000, fraction = 2,message = "数据格式错误")
    @NotNull(message = "数据内容为空")
    @ExcelProperty(value = "2月/元")
    @Min(value = 0,message = "数据格式错误")
    private BigDecimal targetFeb;

    /**
     * 3月份目标
     */
    @Digits(integer = 100000000, fraction = 2,message = "数据格式错误")
    @NotNull(message = "数据内容为空")
    @ExcelProperty(value = "3月/元")
    @Min(value = 0,message = "数据格式错误")
    private BigDecimal targetMar;

    /**
     * 4月份目标
     */
    @Digits(integer = 100000000, fraction = 2,message = "数据格式错误")
    @NotNull(message = "数据内容为空")
    @ExcelProperty(value = "4月/元")
    @Min(value = 0,message = "数据格式错误")
    private BigDecimal targetApr;

    /**
     * 5月份目标
     */
    @Digits(integer = 100000000, fraction = 2,message = "数据格式错误")
    @NotNull(message = "数据内容为空")
    @ExcelProperty(value = "5月/元")
    @Min(value = 0,message = "数据格式错误")
    private BigDecimal targetMay;

    /**
     * 6月份目标
     */
    @Digits(integer = 100000000, fraction = 2,message = "数据格式错误")
    @NotNull(message = "数据内容为空")
    @ExcelProperty(value = "6月/元")
    @Min(value = 0,message = "数据格式错误")
    private BigDecimal targetJun;

    /**
     * 7月份目标
     */
    @Digits(integer = 100000000, fraction = 2,message = "数据格式错误")
    @NotNull(message = "数据内容为空")
    @ExcelProperty(value = "7月/元")
    @Min(value = 0,message = "数据格式错误")
    private BigDecimal targetJul;

    /**
     * 8月份目标
     */
    @Digits(integer = 100000000, fraction = 2,message = "数据格式错误")
    @NotNull(message = "数据内容为空")
    @ExcelProperty(value = "8月/元")
    @Min(value = 0,message = "数据格式错误")
    private BigDecimal targetAug;

    /**
     * 9月份目标
     */
    @Digits(integer = 100000000, fraction = 2,message = "数据格式错误")
    @NotNull(message = "数据内容为空")
    @ExcelProperty(value = "9月/元")
    @Min(value = 0,message = "数据格式错误")
    private BigDecimal targetSep;

    /**
     * 10月份目标
     */
    @Digits(integer = 100000000, fraction = 2,message = "数据格式错误")
    @NotNull(message = "数据内容为空")
    @ExcelProperty(value = "10月/元")
    @Min(value = 0,message = "数据格式错误")
    private BigDecimal targetOct;

    /**
     * 11月份目标
     */
    @Digits(integer = 100000000, fraction = 2,message = "数据格式错误")
    @NotNull(message = "数据内容为空")
    @ExcelProperty(value = "11月/元")
    @Min(value = 0,message = "数据格式错误")
    private BigDecimal targetNov;


    /**
     * 12月份目标
     */
    @Digits(integer = 100000000, fraction = 2,message = "数据格式错误")
    @NotNull(message = "数据内容为空")
    @ExcelProperty(value = "12月/元")
    @Min(value = 0,message = "数据格式错误")
    private BigDecimal targetDec;

}
