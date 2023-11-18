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
 * 运营中台订单明细导出
 */

@Data
@EqualsAndHashCode
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
public class ExportOrderCenterDetailAdminModel {

    /**
     * 订单编号
     */
    @ExcelProperty(value = "订单编号")
    private String orderNo;


    /**
     * 供应商ID
     */
    @ExcelProperty(value = "供应商ID")
    private Long sellerEid;

    /**
     * 供应商名称
     */
    @ExcelProperty(value = "供应商名称")
    private String sellerEname;

    /**
     * 供应商标签
     */
    @ExcelProperty(value = "供应商标签")
    private String sellerLag;

    /**
     * 供应商所在省
     */
    @ExcelProperty(value = "供应商所在省")
    private String sellerProvinceName;

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
     * 收货时间
     */
    @ExcelProperty(value = "收货时间")
    private String receiveTime;

    /**
     * 订单状态
     */
    @ExcelProperty(value = "订单状态")
    private String orderStatusName;

    /**
     * 支付方式
     */
    @ExcelProperty(value = "支付方式")
    private String paymentMethodName;

    /**
     * 支付状态
     */
    @ExcelProperty(value = "支付状态")
    private String paymentStatusName;

    /**
     * 订单来源
     */
    @ExcelProperty(value = "订单来源")
    private String orderSourceName;

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
     * 规格型号
     */
    @ExcelProperty(value = "规格型号")
    private String goodsSpecification;

    /**
     * 购买数量
     */
    @ExcelProperty(value = "购买数量")
    private Integer goodsQuantity;

    /**
     * 发货数量
     */
    @ExcelProperty(value = "发货数量")
    private Integer batchGoodsQuantity;

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
     * 商品原价
     */
    @ExcelProperty(value = "商品原价")
    private BigDecimal originalPrice;

    /**
     * 商品单价
     */
    @ExcelProperty(value = "商品成交价")
    private BigDecimal goodsPrice;

    /**
     * 批次金额小计
     */
    @ExcelProperty(value = "金额小计")
    private BigDecimal goodsAmount;

    /**
     * 支付金额
     */
    @ExcelProperty(value = "支付金额")
    private BigDecimal goodsPayAmount;

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
     * 商品促销
     */
    @ExcelProperty(value = "商品促销")
    private String activityType;

    /**
     * 商品促销平台承担比例
     */
    @ExcelProperty(value = "商品促销平台承担比例")
    private BigDecimal platformRatio;

    /**
     * 商品促销商家承担比例
     */
    @ExcelProperty(value = "商品促销商家承担比例")
    private BigDecimal businessRatio;

    /**
     * 平台承担折扣金额
     */
    @ExcelProperty(value = "平台承担折扣金额")
    private BigDecimal platformDiscountAmount;

    /**
     * 商家承担折扣金额
     */
    @ExcelProperty(value = "商家承担折扣金额")
    private BigDecimal businessDiscountAmount;

    /**
     * 商销单价
     */
    @ExcelProperty(value = "商销单价")
    private BigDecimal  goodsSellPrice;

    /**
     * 商销总金额
     */
    @ExcelProperty(value = "商销总金额")
    private BigDecimal  goodsSellAmount;

    /**
     * 供货单价
     */
    @ExcelProperty(value = "供货单价")
    private BigDecimal  goodsSupportPrice;

    /**
     * 供货总金额
     */
    @ExcelProperty(value = "供货总金额")
    private BigDecimal  goodsSupportAmount;





}