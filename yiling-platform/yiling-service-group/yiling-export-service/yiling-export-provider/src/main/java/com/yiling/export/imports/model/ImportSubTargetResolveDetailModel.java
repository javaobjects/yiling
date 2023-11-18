package com.yiling.export.imports.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.yiling.export.excel.model.BaseImportModel;
import com.yiling.framework.common.annotations.ExcelShow;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: GXL
 * @date: 2023-04-17
 */
@Data
@Accessors(chain = true)
public class ImportSubTargetResolveDetailModel extends BaseImportModel {
    @NotNull
    @ExcelShow
    @Excel(name = "分解编码", orderNum = "1")
    private Long id;

/*
    */
/**
     * 指标名称
     *//*

    @Excel(name = "指标名称", orderNum = "3")
    private String name;

    */
/**
     * 指标编号
     *//*

    @Excel(name = "指标编码", orderNum = "2")
    private String targetNo;
    */
/**
     * 标签为省区的部门名称
     *//*

    @Excel(name = "业务省区", orderNum = "4")
    private String departProvinceName;

    */
/**
     * 部门名称
     *//*

    @Excel(name = "事业部", orderNum = "5")
    private String departName;




    */
/**
     * 标签为区办的部门ID
     *//*

    @Excel(name = "区办编码", orderNum = "6")
    private Long departRegionId;

    */
/**
     * 标签为区办的部门名称
     *//*

    @Excel(name = "区办", orderNum = "7")
    private String departRegionName;
    */
/**
     * 上级主管编码
     *//*

    @Excel(name = "主管工号", orderNum = "8")
    private String superiorSupervisorCode;

    */
/**
     * 上级主管名称
     *//*

    @Excel(name = "主管姓名", orderNum = "9")
    private String superiorSupervisorName;

    */
/**
     * 代表编码
     *//*

    @Excel(name = "代表工号", orderNum = "10")
    private String representativeCode;

    */
/**
     * 代表名称
     *//*

    @Excel(name = "代表姓名", orderNum = "11")
    private String representativeName;
    */
/**
     * 岗位代码
     *//*

    @Excel(name = "岗位编码", orderNum = "12")
    private Long postCode;

    */
/**
     * 岗位名称
     *//*

    @Excel(name = "岗位名称", orderNum = "13")
    private String postName;
    */
/**
     * 机构编码
     *//*

    @Excel(name = "客户编码", orderNum = "14")
    private String customerCode;

    */
/**
     * 机构名称
     *//*

    @Excel(name = "客户名称", orderNum = "15")
    private String customerName;

    */
/**
     * 供应链类型：1商业公司 2医疗机构 3零售机构
     *//*

    private Integer supplyChainRole;
    */
/**
     * 品类ID
     *//*

    private Long categoryId;

    */
/**
     * 品类名称
     *//*

    private String categoryName;

    */
/**
     * 商品ID
     *//*

    private Long goodsId;

    */
/**
     * 商品名称
     *//*

    private String goodsName;

    */
/**
     * 产品组id
     *//*

    private Long productGroupId;

    */
/**
     * 产品组名称
     *//*

    private String productGroupName;


*/







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
