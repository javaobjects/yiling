package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 中台订单导出
 * @author:wei.wang
 * @date:2021/11/10
 */
@Data
public class ExportOrderB2BCenterBO {
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
     * 采购商所在省
     */
    private String provinceName;

    /**
     * 采购商所在市
     */
    private String cityName;

    /**
     * 采购商所在区
     */
    private String regionName;

    /**
     * 供应商ID
     */
    private Long sellerEid;

    /**
     * 供应商名称
     */
    private String sellerEname;

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
    private  BigDecimal deliveryAmount;

    /**
     * 折扣总金额
     */
    private BigDecimal discountAmount;

    /**
     * 支付总金额
     */
    private BigDecimal payAmount;

    /**
     * 收货人联系电话
     */
    private String mobile;

    /**
     * 收货人姓名
     */
    private String receiveName;

    /**
     * 收货人时间
     */
    private String receiveTime;

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
     * 使用优惠券
     */
    private String couponActivityInfo;
}
