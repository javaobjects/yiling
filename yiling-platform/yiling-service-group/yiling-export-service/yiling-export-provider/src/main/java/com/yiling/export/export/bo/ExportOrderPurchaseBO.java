package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 采购订单导出
 * @author:wei.wang
 * @date:2021/8/21
 */
@Data
public class ExportOrderPurchaseBO {
    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 采购商ID
     */
    private Long buyerEid;

    /**
     * 采购商名称
     */
    private String buyerEname;

    /**
     * 合同编号
     */
    private String contractNumber;
    /**
     * 下单时间
     */
    private Date createTime;

    /**
     * 订单状态
     */
    private String orderStatusName;

    /**
     * 支付方式
     */
    private String paymentMethodName;

    /**
     * 支付状态
     */
    private String paymentStatusName;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 实际发货货款总金额
     */
    private BigDecimal deliveryAmount;

    /**
     * 折扣总金额
     */
    private BigDecimal discountAmount;

    /**
     * 支付总金额
     */
    private BigDecimal payAmount;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 规格型号
     */
    private String goodsSpecification;

    /**
     * 批准问文号
     */
    private String goodsLicenseNo;

    /**
     * 购买数量
     */
    private Integer goodsQuantity;

    /**
     * 发货数量
     */
    private Integer goodsDeliveryQuantity;

    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;

    /**
     * 金额小计
     */
    private BigDecimal goodsAmount;

    /**
     * 折扣比率
     */
    private BigDecimal goodsDiscountRate;

    /**
     * 折扣金额
     */
    private BigDecimal goodsDiscountAmount;

    /**
     * 支付金额
     */
    private BigDecimal goodsPayAmount;

    /**
     * 批次号/序列号
     */
    private String batchNo;

    /**
     * 有效期至
     */
    private Date expiryDate;

    /**
     * 批次发货数量
     */
    private Integer batchGoodsQuantity;

    /**
     * 收货人联系电话
     */
    private String mobile;

    /**
     * 收货人姓名
     */
    private String receiveName;

    /**
     * 收货人详细地址
     */
    private String address;

    /**
     * 商务负责人ID
     */
    private Long contacterId;

    /**
     * 商务负责人姓名
     */
    private String contacterName;

    /**
     * 供应商ID
     */
    private Long sellerEid;

    /**
     * 供应商名称
     */
    private String sellerEname;
}
