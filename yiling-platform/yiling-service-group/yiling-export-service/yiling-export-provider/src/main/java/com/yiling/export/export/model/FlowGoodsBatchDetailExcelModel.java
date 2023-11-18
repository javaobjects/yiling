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
public class FlowGoodsBatchDetailExcelModel {

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 库存时间
     */
    @ColumnWidth(22)
    @ExcelProperty(value = "库存时间")
    private Date gbDetailTime;

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
     * 原始商品名称
     */
    @ExcelProperty(value = "原始商品名称")
    private String gbName;

    /**
     * 原始商品规格
     */
    @ExcelProperty(value = "原始商品规格")
    private String gbSpecifications;

    /**
     * 原始商品生产厂家
     */
    @ColumnWidth(25)
    @ExcelProperty(value = "原始商品生产厂家")
    private String gbManufacturer;

    /**
     * 原始商品批准文号
     */
    @ColumnWidth(25)
    @ExcelProperty(value = "原始商品批准文号")
    private String gbLicense;

    /**
     * 数量
     */
    @ColumnWidth(10)
    @ExcelProperty(value = "数量")
    private BigDecimal gbNumber;

    /**
     * 单位
     */
    @ColumnWidth(10)
    @ExcelProperty(value = "单位")
    private String gbUnit;

    /**
     * 批号
     */
    @ColumnWidth(15)
    @ExcelProperty(value = "批号")
    private String gbBatchNo;

    /**
     * 生产日期
     */
    @ExcelProperty(value = "生产日期")
    private String gbProduceTime;

    /**
     * 有效期
     */
    @ExcelProperty(value = "有效期")
    private String gbEndTime;
}
