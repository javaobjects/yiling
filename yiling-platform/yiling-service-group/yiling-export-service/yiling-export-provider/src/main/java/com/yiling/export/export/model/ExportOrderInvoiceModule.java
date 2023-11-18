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
 * 发票管理导出
 */
@Data
@EqualsAndHashCode
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(20)
public class ExportOrderInvoiceModule {

    /**
     *  订单Id
     */
    @ExcelProperty(value = "订单ID")
    private Long orderId;

    /**
     * 订单编号
     */
    @ExcelProperty(value = "订单编号")
    private String orderNo;

    /**
     * 下单时间
     */
    @ExcelProperty(value = "下单时间")
    private Date createTime;

    /**
     * 支付方式
     */
    @ExcelProperty(value = "支付方式")
    private String paymentMethodName;

    /**
     * 实际发货货款总金额
     */
    @ExcelProperty(value = "实际发货货款总金额")
    private BigDecimal deliveryAmount;

    /**
     * 折扣总金额
     */
    @ExcelProperty(value = "折扣总金额")
    private BigDecimal discountAmount;

    /**
     * 发票总金额
     */
    @ExcelProperty(value = "发票总金额")
    private  BigDecimal invoiceAllAmount;

    /**
     * 发票状态
     */
    @ExcelProperty(value = "发票状态")
    private String invoiceStatusName;

    /**
     * 发票申请人ID
     */
    @ExcelProperty(value = "发票申请人ID")
    private Long invoiceApplyUserId;

    /**
     * 发票申请人姓名
     */
    @ExcelProperty(value = "发票申请人姓名")
    private String invoiceApplyUserName;

    /**
     * 发票申请时间
     */
    @ExcelProperty(value = "发票申请时间")
    private Date invoiceApplyTime;

    /**
     * 发票张数
     */
    @ExcelProperty(value = "发票张数")
    private Integer invoiceNumber;

    /**
     * 发票单号
     */
    @ExcelProperty(value = "发票单号")
    private String invoiceNo;

    /**
     * 发票金额
     */
    @ExcelProperty(value = "发票金额")
    private BigDecimal invoiceAmount;

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
     * 供应商ID
     */
    @ExcelProperty(value = "供应商ID")
    private Long sellerEid;

    /**
     * 供应商名称
     */
    @ExcelProperty(value = "供应商名称")
    private String sellerEname;


}
