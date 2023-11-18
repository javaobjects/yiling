package com.yiling.sales.assistant.app.invoice.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售助手发票管理列表
 * @author:wei.wang
 * @date:2021/9/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderInvoiceListPageVO extends BaseVO {
    /**
     * 配送商名称
     */
    @ApiModelProperty(value = "配送商名称")
    private String distributorEname;

    /**
     * 发票金额
     */
    @ApiModelProperty(value = "发票金额")
    private BigDecimal invoiceAmount;

    /**
     * 发票申请时间
     */
    @ApiModelProperty(value = "发票申请时间")
    private Date invoiceTime;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 支付金额
     */
    @ApiModelProperty(value = "支付总金额")
    private BigDecimal paymentAmount;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款")
    private Integer paymentMethod;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private Date createTime;

    /**
     * 货款总额
     */
    @ApiModelProperty(value = "货款总金额")
    private BigDecimal totalAmount;

    /**
     * 折扣金额
     */
    @ApiModelProperty(value = "折扣总金额")
    private BigDecimal discountAmount;

    /**
     * 发票数量
     */
    @ApiModelProperty(value = "发票数量")
    private Integer invoiceNumber;

    /**
     * 购买商品数量
     */
    @ApiModelProperty(value = "购买商品数量")
    private Integer purchaseGoodsNumber;
}
