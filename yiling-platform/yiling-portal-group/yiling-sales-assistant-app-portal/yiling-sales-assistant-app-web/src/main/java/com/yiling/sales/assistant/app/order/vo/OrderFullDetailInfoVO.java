package com.yiling.sales.assistant.app.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 销售助手订单详情信息
 * zhigang.guo
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderFullDetailInfoVO extends BaseVO {

    @ApiModelProperty(value = "是否为以岭直采")
    private Boolean isYilingFlag;

    @ApiModelProperty(value = "购买企业ID")
    private Long buyerEid;


    @ApiModelProperty(value = "商品信息")
    private List<OrderDetailVO> orderDetailDelivery;
    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     * 商品总金额
     */
    @ApiModelProperty(value = "商品总金额")
    private BigDecimal totalAmount;

    /**
     * 应付金额
     */
    @ApiModelProperty(value = "应付总金额")
    private BigDecimal paymentAmount;

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
     * 配送商企业ID
     */
    @ApiModelProperty(value = "配送商企业ID")
    private Long distributorEid;

    /**
     * 配送商名称
     */
    @ApiModelProperty(value = "配送商名称")
    private String distributorEname;

    @ApiModelProperty(value = "开票时间")
    private Date invoiceTime;

    /**
     * 开票状态：1-待申请 2-已申请 3-已开票 4-申请驳回 5-已作废
     */
    @ApiModelProperty(value = "开票状态")
    private Integer invoiceStatus;

    /**
     * 开票金额
     */
    @ApiModelProperty(value = "开票金额")
    private BigDecimal invoiceAmount;

    /**
     *折扣总金额
     */
    @ApiModelProperty(value = "折扣总金额")
    private BigDecimal discountAmount;

    /**
     * 购买商品种类
     */
    @ApiModelProperty(value = "购买商品种类")
    private Integer goodsOrderNum;

    /**
     * 购买商品件数
     */
    @ApiModelProperty(value = "购买商品件数")
    private Integer goodsOrderPieceNum;

    /**
     * 发货商品种类
     */
    @ApiModelProperty(value = "发货商品种类")
    private Integer deliveryOrderNum;

    /**
     * 发货商品件数
     */
    @ApiModelProperty(value = "发货商品件数")
    private Integer deliveryOrderPieceNum;


    /**
     * 收获商品种类
     */
    @ApiModelProperty(value = "收获商品种类")
    private Integer receiveOrderNum;

    /**
     * 收获商品件数
     */
    @ApiModelProperty(value = "收获商品件数")
    private Integer receiveOrderPieceNum;

    /**
     * 订单状态：10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消 -20-待转发，-30 待客户确认 -40 审核驳回
     */
    @ApiModelProperty(value = "订单状态：-50未提交 10-待审核 20-待发货 25-部分发货 30-已发货 40-已收货 100-已完成 -10-已取消 -20-待转发，-30 待客户确认 -40 审核驳回")
    private Integer orderStatus;

    @ApiModelProperty(value = "下单备注")
    private String orderNote;

}
