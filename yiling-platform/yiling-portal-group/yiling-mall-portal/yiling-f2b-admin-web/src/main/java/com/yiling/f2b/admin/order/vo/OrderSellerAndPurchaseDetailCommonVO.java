package com.yiling.f2b.admin.order.vo;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 采购订单，销售订单 详情公用返回VO
 *
 * @author:wei.wang
 * @date:2021/6/22
 */
@Data
public class OrderSellerAndPurchaseDetailCommonVO implements java.io.Serializable {


    @ApiModelProperty(value = "订单ID")
    private Long   id;
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 商品总金额
     */
    @ApiModelProperty(value = "货款总金额")
    private BigDecimal totalAmount;

    /**
     * 应付金额
     */
    @ApiModelProperty(value = "支付总金额")
    private BigDecimal paymentAmount;

    /**
     *采购商ID
     */
    @ApiModelProperty(value = "采购商ID")
    private Long buyerEid;

    /**
     *采购商名称
     */
    @ApiModelProperty(value = "采购商名称")
    private String buyerEname;


    /**
     *供应商ID
     */
    @ApiModelProperty(value = "供应商ID")
    private Long sellerEid;

    /**
     *供应商名称
     */
    @ApiModelProperty(value = "供应商名称")
    private String sellerEname;




    /**
     * 票折金额
     */
    @ApiModelProperty(value = "票折总金额")
    private BigDecimal ticketDiscountAmount;

    /**
     * 现折金额
     */
    @ApiModelProperty(value = "现折总金额")
    private BigDecimal cashDiscountAmount;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款")
    private Integer paymentMethod;

    /**
     * 支付状态：1-待支付 2-已支付
     */
    @ApiModelProperty(value = "支付状态：1-待支付 2-已支付")
    private Integer paymentStatus;


    /**
     * 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    @ApiModelProperty(value = "订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消")
    private Integer orderStatus;

    /**
     * 下单备注
     */
    @ApiModelProperty(value = "下单备注")
    private String orderNote;

    /**
     * 物流类型：1-自有物流 2-第三方物流
     */
    @ApiModelProperty(value = "物流类型：1-自有物流 2-第三方物流")
    private Integer deliveryType;

    /**
     * 物流公司
     */
    @ApiModelProperty(value = "物流公司")
    private String deliveryCompany;



    /**
     * 物流单号
     */
    @ApiModelProperty(value = "物流单号")
    private String deliveryNo;

    @ApiModelProperty(value = "票折信息")
    List<OrderTicketDiscountVO> ticketDiscountList;

    @ApiModelProperty(value = "订单状态变化")
    private List<OrderLogVO> orderLogInfo;

    @ApiModelProperty(value = "商品信息")
    private List<OrderDetailDeliveryVO> orderDetailDelivery;

    @ApiModelProperty(value = "发票信息")
    private OrderInvoiceApplyAllInfoVO orderInvoiceApplyAllInfo;

    @ApiModelProperty(value = "是否属于以岭,true是，false否")
    private Boolean YLFlag;

    /**
     * 合同编号
     */
    @ApiModelProperty(value = "合同编号")
    private String contractNumber;

}

