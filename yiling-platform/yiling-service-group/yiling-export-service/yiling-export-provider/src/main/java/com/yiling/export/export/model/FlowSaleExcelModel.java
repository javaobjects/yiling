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
public class FlowSaleExcelModel {

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 销售时间
     */
    @ColumnWidth(22)
    @ExcelProperty(value = "销售时间")
    private Date soTime;

    /**
     * 销售单号
     */
    @ExcelProperty(value = "销售单号")
    private String soNo;

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
     * 客户名称
     */
    @ColumnWidth(30)
    @ExcelProperty(value = "客户名称")
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
    private String soSpecifications;

    /**
     * 原始商品生产厂家
     */
    @ColumnWidth(25)
    @ExcelProperty(value = "原始商品生产厂家")
    private String soManufacturer;

    /**
     * 原始商品批准文号
     */
    @ColumnWidth(25)
    @ExcelProperty(value = "原始商品批准文号")
    private String soLicense;

    /**
     * 数量
     */
    @ColumnWidth(10)
    @ExcelProperty(value = "数量")
    private BigDecimal soQuantity;

    /**
     * 单位
     */
    @ColumnWidth(10)
    @ExcelProperty(value = "单位")
    private String soUnit;

    /**
     * 单价
     */
    @ColumnWidth(10)
    @ExcelProperty(value = "单价")
    private BigDecimal soPrice;

    /**
     * 金额
     */
    @ExcelProperty(value = "金额")
    private BigDecimal soTotalAmount;

    /**
     * 批号
     */
    @ColumnWidth(15)
    @ExcelProperty(value = "批号")
    private String soBatchNo;

    /**
     * 数据标签 0-正常 1-(异常)新增 2-(异常)修改 3-已删除
     */
    @ExcelProperty(value = "数据标签")
    private String dataTag;
}
