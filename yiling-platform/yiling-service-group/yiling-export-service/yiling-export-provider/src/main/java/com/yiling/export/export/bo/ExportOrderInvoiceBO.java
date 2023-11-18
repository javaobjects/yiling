package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 发票导出BO
 * @author:wei.wang
 * @date:2021/8/23
 */@Data
public class ExportOrderInvoiceBO {

    /**
     *  订单Id
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 下单时间
     */
    private Date createTime;

    /**
     * 支付方式
     */
    private String paymentMethodName;

    /**
     * 实际发货货款总金额
     */
    private BigDecimal deliveryAmount;

    /**
     * 折扣总金额
     */
    private BigDecimal discountAmount;

    /**
     * 发票总金额
     */
    private  BigDecimal invoiceAllAmount;

    /**
     * 发票状态
     */
    private String invoiceStatusName;

    /**
     * 发票申请人ID
     */
    private Long invoiceApplyUserId;

    /**
     * 发票申请人姓名
     */
    private String invoiceApplyUserName;

    /**
     * 发票申请时间
     */
    private Date invoiceApplyTime;

    /**
     * 发票张数
     */
    private Integer invoiceNumber;

    /**
     * 发票单号
     */
    private String invoiceNo;

    /**
     * 发票金额
     */
    private BigDecimal invoiceAmount;

    /**
     * 采购商ID
     */
    private Long buyerEid;

    /**
     * 采购商名称
     */
    private String buyerEname;

    /**
     * 供应商ID
     */
    private Long sellerEid;

    /**
     * 供应商名称
     */
    private String sellerEname;
}
