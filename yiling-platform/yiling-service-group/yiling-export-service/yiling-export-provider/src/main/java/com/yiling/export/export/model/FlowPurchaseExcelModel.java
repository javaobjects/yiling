package com.yiling.export.export.model;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucheng.bai
 * @date 2023/3/28
 */
@Data
@EqualsAndHashCode
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
public class FlowPurchaseExcelModel {

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 采购时间
     */
    @ColumnWidth(22)
    @ExcelProperty(value = "采购时间")
    private Date poTime;

    /**
     * 采购单号
     */
    @ExcelProperty(value = "采购单号")
    private String poNo;

    /**
     * 经销商级别
     */
    @ExcelProperty(value = "经销商级别")
    private String supplierLevel;

    /**
     * 经销商名称
     */
    @ColumnWidth(30)
    @ExcelProperty(value = "经销商名称")
    private String ename;

    /**
     * 供应商名称
     */
    @ColumnWidth(30)
    @ExcelProperty(value = "供应商名称")
    private String enterpriseName;

    /**
     * 商品名称
     */
    @ExcelProperty(value = "原始商品名称")
    private String goodsName;

    /**
     * 商品规格
     */
    @ExcelProperty(value = "原始商品规格")
    private String poSpecifications;

    /**
     * 原始商品生产厂家
     */
    @ColumnWidth(25)
    @ExcelProperty(value = "原始商品生产厂家")
    private String poManufacturer;

    /**
     * 原始商品批准文号
     */
    @ColumnWidth(25)
    @ExcelProperty(value = "原始商品批准文号")
    private String poLicense;

    /**
     * 数量
     */
    @ColumnWidth(10)
    @ExcelProperty(value = "数量")
    private BigDecimal poQuantity;

    /**
     * 单位
     */
    @ColumnWidth(10)
    @ExcelProperty(value = "单位")
    private String poUnit;

    /**
     * 采购单价
     */
    @ColumnWidth(10)
    @ExcelProperty(value = "采购单价")
    private BigDecimal poPrice;

    /**
     * 采购金额
     */
    @ExcelProperty(value = "采购金额")
    private BigDecimal poTotalAmount;

    /**
     * 批号
     */
    @ColumnWidth(15)
    @ExcelProperty(value = "批号")
    private String poBatchNo;

    /**
     * 数据标签 0-正常 1-(异常)新增 2-(异常)修改 3-已删除
     */
    @ExcelProperty(value = "数据标签")
    private String dataTag;
}
