package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 预订单列表导出DTO
 * @author:wei.wang
 * @date:2021/8/20
 */
@Data
public class OrderExpectExportDTO  implements java.io.Serializable {
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
     * 审核状态
     */
    private Integer auditStatus;

    /**
     * 审核状态名称
     */
    private String auditStatusName;

    /**
     * 审核人ID
     */
    private Long auditUser;

    /**
     * 审核人姓名
     */
    private String auditName;
    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 支付方式
     */
    private Long paymentMethod;

    /**
     * 支付方式名称
     */
    private String paymentMethodName;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

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
     * 批准文号
     */
    private String goodsLicenseNo;

    /**
     * 购买数量
     */
    private Integer goodsQuantity;

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
