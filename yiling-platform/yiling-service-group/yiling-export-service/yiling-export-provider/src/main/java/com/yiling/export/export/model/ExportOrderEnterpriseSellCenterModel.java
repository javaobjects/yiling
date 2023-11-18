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
 * 商家后台企业订单导出
 * @author:wei.wang
 * @date:2023/4/6
 */
@Data
@EqualsAndHashCode
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
public class ExportOrderEnterpriseSellCenterModel {
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
     * 订单总金额
     */
    @ExcelProperty(value = "订单总金额")
    private BigDecimal totalAmount;

    /**
     * 实际发货货款总金额
     */
    @ExcelProperty(value = "实际发货货款总金额")
    private  BigDecimal deliveryAmount;

    /**
     * 折扣总金额
     */
    @ExcelProperty(value = "折扣总金额")
    private BigDecimal discountAmount;

    /**
     * 支付总金额
     */
    @ExcelProperty(value = "支付总金额")
    private BigDecimal payAmount;

    /**
     * 收货人联系电话
     */
    @ExcelProperty(value = "收货人联系电话")
    private String mobile;

    /**
     * 收货人姓名
     */
    @ExcelProperty(value = "收货人姓名")
    private String receiveName;

    /**
     * 收货人详细地址
     */
    @ExcelProperty(value = "收货人详细地址")
    private String address;

    /**
     * 商务负责人ID
    @ExcelProperty(value = "商务负责人ID")
    private Long contacterId;

    *//**
     * 商务负责人姓名
     *//*
    @ExcelProperty(value = "商务负责人姓名")
    private String contacterName;*/

    /**
     * 使用优惠券
     */
    @ExcelProperty(value = "使用优惠券")
    private String couponActivityInfo;
}
