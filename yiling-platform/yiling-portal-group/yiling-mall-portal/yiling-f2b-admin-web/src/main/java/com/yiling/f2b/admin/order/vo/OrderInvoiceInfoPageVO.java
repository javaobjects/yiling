package com.yiling.f2b.admin.order.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 发票管理列表
 * @author:wei.wang
 * @date:2021/7/6
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderInvoiceInfoPageVO extends BaseDTO {
    /**
     * 采购商名称
     */
    @ApiModelProperty(value = "采购商名称")
    private String buyerEname;
    /**
     * 采购商eid
     */
    @ApiModelProperty(value = "采购商eid")
    private Long buyerEid;

    /**
     * 配送商名称
     */
    @ApiModelProperty(value = "配送商名称")
    private String distributorEname;

    /**
     * 配送商eid
     */
    @ApiModelProperty(value = "配送商eid")
    private Long distributorEid;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private Date createTime;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款 4-线上支付")
    private Integer paymentMethod;

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
     * 支付金额
     */
    @ApiModelProperty(value = "支付总金额")
    private BigDecimal paymentAmount;

    /**
     * 开票状态：1-待申请 2-已申请 3-已开票 4-申请驳回 5-已作废
     */
    @ApiModelProperty(value = "开票状态：1-待申请 2-已申请 3-已开票 4-申请驳回 5-已作废")
    private Integer invoiceStatus;

    /**
     * 订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    @ApiModelProperty(value = "订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消")
    private Integer orderStatus;

    /**
     * 发票号码
     */
    @ApiModelProperty(value = "发票号码")
    private String invoiceNo;

    @ApiModelProperty(value = "已开票金额")
    private BigDecimal invoiceFinishAmount;
}
