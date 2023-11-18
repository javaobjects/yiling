package com.yiling.sales.assistant.app.invoice.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 申请发票和修改发票页面VO
 * @author:wei.wang
 * @date:2021/7/2
 */
@Data
public class OrderApplyInvoiceVO {

    /**
     * 商品信息
     */
    @ApiModelProperty(value = "商品信息")
    private List<OrderApplyInvoiceDetailVO> applyInvoiceDetailInfo;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 商品总金额
     */
    @ApiModelProperty(value = "货款金额")
    private BigDecimal totalAmount;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款")
    private Integer paymentMethod;



    /**
     * 发票金额
     */
    @ApiModelProperty(value = "发票金额")
    private BigDecimal invoiceAmount;



    /**
     * 是否使用票折：0-否 1-是
     */
    @ApiModelProperty(value = "是否使用票折：0-否 1-是")
    private Integer ticketDiscountFlag;

    /**
     * 票折总金额
     */
    @ApiModelProperty(value = "票折总金额")
    private BigDecimal ticketDiscountAllAmount;

    @ApiModelProperty(value = "现折金额")
    private BigDecimal cashDiscountAmount;
    /**
     * 选择票折信息
     */
    @ApiModelProperty(value = "选择票折信息")
    private List<OrderChoiceTicketInfoVO> orderTicketInfo;

    /**
     * 转换规则编码
     */
    @ApiModelProperty(value = "转换规则编码")
    private String transitionRuleCode;
}
