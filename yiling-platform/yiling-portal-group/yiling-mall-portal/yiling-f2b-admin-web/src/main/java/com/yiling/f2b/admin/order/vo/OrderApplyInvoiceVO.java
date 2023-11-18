package com.yiling.f2b.admin.order.vo;

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


    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 商品总金额
     */
    @ApiModelProperty(value = "货款总金额")
    private BigDecimal totalAmount;

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
     * 订单状态：10-待审核 20-待发货 25-部分发货  30-已发货 40-已收货 100-已完成 -10-已取消
     */
    @ApiModelProperty(value = "订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消")
    private Integer orderStatus;

    /**
     * 开票状态：1-待申请 2-部分申请  4-部分开票 3-已开票 5-已作废
     */
    @ApiModelProperty(value ="开票状态：1-待申请 2-部分申请  4-部分开票 3-已开票 5-已作废")
    private Integer invoiceStatus;

    /**
     * 出库单明细商品
     */
    @ApiModelProperty(value ="出库单明细商品")
    private List<OrderInvoiceErpDeliveryNoVO> orderInvoiceErpDeliveryNoList;


    /**
     * 电子发票邮箱
     */
    @ApiModelProperty(value = "电子发票邮箱")
    private String invoiceEmail;


    /**
     * 转换规则编码
     */
    @ApiModelProperty(value = "转换规则编码")
    private String transitionRuleCode;

    /**
     * 开票最大限制金额
     */
    @ApiModelProperty(value = "开票最大限制金额")
    private BigDecimal invoiceMaxAmount;

    /**
     * 买家名称
     */
    @ApiModelProperty(value = "买家名称")
    private String buyerEname;

    /**
     * 卖家名称
     */
    @ApiModelProperty(value = "卖家名称")
    private String sellerEname;

}
