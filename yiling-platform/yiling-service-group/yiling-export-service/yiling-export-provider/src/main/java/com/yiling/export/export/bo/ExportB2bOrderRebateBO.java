package com.yiling.export.export.bo;

import java.math.BigDecimal;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 运营后台中台数据管理订单返利报表
 *
 * @author gaoxinlei
 * @date 2022-02-25
 */
@Data
public class ExportB2bOrderRebateBO {
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
     * 支付状态
     */
    private String paymentStatus;

    /**
     * 支付类型：1-线下支付 2-在线支付
     */
    private String paymentType;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    private String paymentMethod;

    /**
     * 在线支付渠道
     */
    private String payChannel;




    /**
     * 收货时间
     */
    private Date receiveTime;


    /**
     * 下单时间
     */
    private Date createTime;

    /**
     * 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    private String orderStatus;

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
     * 收货数量
     */
    private Integer receiveQuantity;

    private Integer returnQuantity;

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
    private Integer deliveryQuantity;
    /**
     * 商品类型
     */
    private String category;


    /**
     * 供货价
     */
    private BigDecimal supplyPrice;

    /**
     * 出货价
     */
    private BigDecimal outPrice;

    /**
     * 销售额返利金额
     */
    private BigDecimal rebateMoney;

    /**
     * 商销价
     */
    private BigDecimal  goodsSellPrice;

    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;

    /**
     * 商品原价
     */
    private BigDecimal originalPrice;

    /**
     * 商品小计
     */
    private BigDecimal goodsAmount;

    /**
     * 折扣金额
     */
    private BigDecimal sellAmount;

    /**
     * 折扣比率
     */
    private BigDecimal sellPercentage;

    /**
     * 支付金额
     */
    private BigDecimal paidAmount;

    /**
     * 促销活动类型
     */
    private String activityType;

    /**
     * 活动内容
     */
    private String activityContent;

    /**
     * 平台承担折扣比例
     */
    private BigDecimal platformPercentage;

    /**
     * 平台承担折扣金额
     */
    private BigDecimal platformAmount;

    /**
     * 商家承担折扣比例
     */
    private BigDecimal shopPercentage;

    /**
     * 商家承担折扣金额
     */
    private BigDecimal shopAmount;

}
