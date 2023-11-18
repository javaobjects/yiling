package com.yiling.export.export.model;

import java.math.BigDecimal;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: GXL
 * @date: 2023-04-17
 */
@Data
@Accessors(chain = true)
public class TargetResolveModel {
    @ExcelProperty(value = "分解编码")
    private Long id;

    @ExcelProperty(value = "指标编号")
    private String targetNo;

    @ExcelProperty(value = "指标名称")
    private String name;


    @ExcelProperty(value = "业务省区")
    private String departProvinceName;


    @ExcelProperty(value = "事业部")
    private String departName;


    @ExcelProperty(value = "区办")
    private String departRegionName;


    @ExcelProperty(value = "主管工号")
    private String superiorSupervisorCode;



    @ExcelProperty(value = "主管姓名")
    private String superiorSupervisorName;



    @ExcelProperty(value = "代表工号")
    private String representativeCode;



    @ExcelProperty(value = "代表姓名")
    private String representativeName;



    @ExcelProperty(value = "岗位编码")
    private Long postCode;


    @ExcelProperty(value = "岗位名称")
    private String postName;


    @ExcelProperty(value = "客户编码")
    private String customerCode;


    @ExcelProperty(value = "客户名称")
    private String customerName;

    @ExcelProperty(value = "供应链角色")
    private String supplyChainRoleStr;


    @ExcelProperty(value = "城市")
    private String city;
    @ExcelProperty(value = "区县")
    private String districtCounty;

    @ExcelProperty(value = "品种")
    private String categoryName;

    @ExcelProperty(value = "产品编码")
    private Long goodsId;

    @ExcelProperty(value = "产品名称")
    private String goodsName;

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
