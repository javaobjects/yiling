package com.yiling.dataflow.sale.bo;

import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: GXL
 * @date: 2023-04-17
 */
@Data
@Accessors(chain = true)
@ExcelTarget("exportSubTargetResolveDetailBO")
public class ExportSubTargetResolveDetailBO  {
    @Excel(name = "分解编码", orderNum = "1")
    private Long id;



    @Excel(name = "指标名称", orderNum = "3")
    private String name;


    @Excel(name = "指标编号", orderNum = "2")
    private String targetNo;

    @Excel(name = "业务省区", orderNum = "4")
    private String departProvinceName;



    @Excel(name = "事业部", orderNum = "5")
    private String departName;





    @Excel(name = "区办", orderNum = "7")
    private String departRegionName;


    @Excel(name = "主管工号", orderNum = "8")
    private String superiorSupervisorCode;



    @Excel(name = "主管姓名", orderNum = "9")
    private String superiorSupervisorName;



    @Excel(name = "代表工号", orderNum = "10")
    private String representativeCode;



    @Excel(name = "代表姓名", orderNum = "11")
    private String representativeName;



    @Excel(name = "岗位编码", orderNum = "12")
    private Long postCode;


    @Excel(name = "岗位名称", orderNum = "13")
    private String postName;


    @Excel(name = "客户编码", orderNum = "14")
    private String customerCode;


    @Excel(name = "客户名称", orderNum = "15")
    private String customerName;

    @Excel(name = "供应链角色", orderNum = "16")
    private String supplyChainRoleStr;


    @Excel(name = "城市", orderNum = "17")
    private String city;
    @Excel(name = "区县", orderNum = "18")
    private String districtCounty;

    @Excel(name = "品种", orderNum = "19")
    private String categoryName;

    @Excel(name = "产品编码", orderNum = "20")
    private Long goodsId;

    @Excel(name = "产品名称", orderNum = "21")
    private String goodsName;


    /**
     * 1月份目标
     */
    @Excel(name = "1月/元", orderNum = "22")
    private BigDecimal targetJan;

    /**
     * 2月份目标
     */
    @Excel(name = "2月/元", orderNum = "23")
    private BigDecimal targetFeb;

    /**
     * 3月份目标
     */
    @Excel(name = "3月/元", orderNum = "24")
    private BigDecimal targetMar;

    /**
     * 4月份目标
     */
    @Excel(name = "4月/元", orderNum = "25")
    private BigDecimal targetApr;

    /**
     * 5月份目标
     */
    @Excel(name = "5月/元", orderNum = "26")
    private BigDecimal targetMay;

    /**
     * 6月份目标
     */
    @Excel(name = "6月/元", orderNum = "27")
    private BigDecimal targetJun;

    /**
     * 7月份目标
     */
    @Excel(name = "7月/元", orderNum = "28")
    private BigDecimal targetJul;

    /**
     * 8月份目标
     */
    @Excel(name = "8月/元", orderNum = "29")
    private BigDecimal targetAug;

    /**
     * 9月份目标
     */
    @Excel(name = "9月/元", orderNum = "30")
    private BigDecimal targetSep;

    /**
     * 10月份目标
     */
    @Excel(name = "10月/元", orderNum = "31")
    private BigDecimal targetOct;

    /**
     * 11月份目标
     */
    @Excel(name = "11月/元", orderNum = "32")
    private BigDecimal targetNov;

    /**
     * 12月份目标
     */
    @Excel(name = "12月/元", orderNum = "33")
    private BigDecimal targetDec;

}
