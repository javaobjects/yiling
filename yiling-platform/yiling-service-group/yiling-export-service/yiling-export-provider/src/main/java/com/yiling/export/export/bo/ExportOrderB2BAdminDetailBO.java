package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * B2B商家后台订单明细导出
 * @author:wei.wang
 * @date:2021/8/21
 */
@Data
public class ExportOrderB2BAdminDetailBO {
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
     * 下单时间
     */
    private Date createTime;

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
     * 发货数量
     */
    private Integer batchGoodsQuantity;

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

}
