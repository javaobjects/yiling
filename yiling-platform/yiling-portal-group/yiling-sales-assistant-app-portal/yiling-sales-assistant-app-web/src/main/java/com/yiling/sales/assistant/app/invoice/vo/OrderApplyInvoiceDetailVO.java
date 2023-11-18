package com.yiling.sales.assistant.app.invoice.vo;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 申请发票明细页面
 * @author:wei.wang
 * @date:2021/6/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderApplyInvoiceDetailVO extends BaseVO {

    /**
     * 订单ID
     */
    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID")
    private Long goodsId;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品通用名
     */
    @ApiModelProperty(value = "商品通用名")
    private String goodsCommonName;

    /**
     * 商品批准文号
     */
    @ApiModelProperty(value = "商品批准文号")
    private String goodsLicenseNo;

    /**
     * 商品规格
     */
    @ApiModelProperty(value = "商品规格")
    private String goodsSpecification;

    /**
     * 商品生产厂家
     */
    @ApiModelProperty(value = "商品生产厂家")
    private String goodsManufacturer;

    /**
     * 商品单价
     */
    @ApiModelProperty(value = "商品单价")
    private BigDecimal goodsPrice;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer goodsQuantity;

    /**
     * 商品小计
     */
    @ApiModelProperty(value = "商品小计")
    private BigDecimal goodsAmount;

    /**
     * 发货数量
     */
    @ApiModelProperty(value = "发货数量")
    private Integer deliveryQuantity;

    @ApiModelProperty(value = "商品图片")
    private String goodsPic;

    @ApiModelProperty(value = "现折比率")
    private BigDecimal caseDiscountRate;

    @ApiModelProperty(value = "现折金额")
    private BigDecimal cashDiscountAmount;

    @ApiModelProperty(value = "票折金额")
    private BigDecimal ticketDiscountAmount;

    @ApiModelProperty(value = "票折比率")
    private BigDecimal ticketDiscountRate;

    @ApiModelProperty(value = "票折方式：1-按比率 2-按金额")
    private Integer invoiceDiscountType;

    /**
     * 折扣金额
     */
    @ApiModelProperty(value = "折扣金额")
    private BigDecimal goodsDiscountAmount;

    /**
     * 折扣比率
     */
    @ApiModelProperty(value = "折扣比率")
    private BigDecimal goodsDiscountRate;

}
