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
 * B2B商家后端销售订单明细
 */

@Data
@EqualsAndHashCode
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
public class ExportB2BOrderSaleDetailModule {
    /**
     * 订单编号
     */
    @ExcelProperty(value = "订单编号")
    private String orderNo;

    /**
     * 采购商ID
     */
    @ExcelProperty(value = "采购商ID")
    private Long buyerEid;

    /**
     * 采购商名称
     */
    @ExcelProperty(value = "采购商名称")
    private String buyerEname;

    /**
     * 采购商所在省
     */
    @ExcelProperty(value = "采购商所在省")
    private String provinceName;


    /**
     * 采购商所在市
     */
    @ExcelProperty(value = "采购商所在市")
    private String cityName;

    /**
     * 采购商所在区
     */
    @ExcelProperty(value = "采购商所在区")
    private String regionName;

    /**
     * 下单时间
     */
    @ExcelProperty(value = "下单时间")
    private Date createTime;

    /**
     * 商品ID
     */
    @ExcelProperty(value = "商品ID")
    private Long goodsId;


    /**
     * 商品名称
     */
    @ExcelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 购买数量
     */
    @ExcelProperty(value = "购买数量")
    private Integer goodsQuantity;

    /**
     * 规格型号
     */
    @ExcelProperty(value = "规格型号")
    private String goodsSpecification;

    /**
     * 批准文号
     */
    @ExcelProperty(value = "批准文号")
    private String goodsLicenseNo;

    /**
     * 批次号/序列号
     */
    @ExcelProperty(value = "批次号/序列号")
    private String batchNo;

    /**
     * 有效期至
     */
    @ExcelProperty(value = "有效期至")
    private Date expiryDate;

    /**
     * 发货数量
     */
    @ExcelProperty(value = "发货数量")
    private Integer batchGoodsQuantity;

    /**
     * 商品单价
     */
    @ExcelProperty(value = "商品单价")
    private BigDecimal goodsPrice;

    /**
     * 批次金额小计
     */
    @ExcelProperty(value = "金额小计")
    private BigDecimal goodsAmount;

    /**
     * 折扣比率
     */
    @ExcelProperty(value = "折扣比率")
    private BigDecimal goodsDiscountRate;

    /**
     * 折扣金额
     */
    @ExcelProperty(value = "折扣金额")
    private BigDecimal goodsDiscountAmount;

    /**
     * 支付金额
     */
    @ExcelProperty(value = "支付金额")
    private BigDecimal goodsPayAmount;


}
