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
public class ExportOrderB2BCenterDetailBO {
    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 供应商ID
     */
    private Long sellerEid;

    /**
     * 供应商名称
     */
    private String sellerEname;


    /**
     * 采购商ID
     */
    private Long buyerEid;

    /**
     * 采购商名称
     */
    private String buyerEname;

    /**
     * 供应商标签
     */
    private String sellerLag;

    /**
     * 供应商所在省
     */
    private String sellerProvinceName;
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
     * 商务负责人ID
     */
    private Long contacterId;

    /**
     * 商务负责人姓名
     */
    private String contacterName;

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
     * 订单来源
     */
    private String orderSourceName;

    /**
     * 合同编号
     */
    private String contractNumber;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 购买数量
     */
    private Integer goodsQuantity;

    /**
     * 规格型号
     */
    private String goodsSpecification;

    /**
     * 批准文号
     */
    private String goodsLicenseNo;

    /**
     * 批次号/序列号
     */
    private String batchNo;

    /**
     * 有效期至
     */
    private Date expiryDate;

    /**
     * 生产日期
     */
    private Date produceDate;

    /**
     * 生产厂家
     */
    private String goodsManufacturer;

    /**
     * 发货数量
     */
    private Integer batchGoodsQuantity;

    /**
     * 商品原价
     */
    private BigDecimal originalPrice;

    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;

    /**
     * 批次金额小计
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
     * 商销单价
     */
    private BigDecimal  goodsSellPrice;

    /**
     * 商销总金额
     */
    private BigDecimal  goodsSellAmount;

    /**
     * 供货单价
     */
    private BigDecimal  goodsSupportPrice;

    /**
     * 供货总金额
     */
    private BigDecimal  goodsSupportAmount;

    /**
     * 收货时间
     */
    private String receiveTime;

    /**
     * 活动类型
     */
    private String activityType;

    /**
     * 商品促销平台承担比例
     */
    private BigDecimal platformRatio;

    /**
     * 商品促销商家承担比例
     */
    private BigDecimal businessRatio;

    /**
     * 平台承担折扣金额
     */
    private BigDecimal platformDiscountAmount;

    /**
     * 商家承担折扣金额
     */
    private BigDecimal businessDiscountAmount;
}
