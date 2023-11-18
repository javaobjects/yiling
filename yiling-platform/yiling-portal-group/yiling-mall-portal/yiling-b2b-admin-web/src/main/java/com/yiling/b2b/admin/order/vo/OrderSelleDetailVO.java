package com.yiling.b2b.admin.order.vo;

import java.math.BigDecimal;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 销售订单详情VO
 * @author:wei.wang
 * @date:2021/6/22
 */
@Data
public class OrderSelleDetailVO extends BaseVO {

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
     *原价总金额
     */
    @ApiModelProperty(value = "原价总金额")
    private BigDecimal originalAmount;

    /**
     * 应付金额
     */
    @ApiModelProperty(value = "支付总金额")
    private BigDecimal paymentAmount;

    /**
     * 优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountAmount;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款
     */
    @ApiModelProperty(value = "支付方式：1-线下支付 2-账期 3-预付款 4-线上支付")
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


    @ApiModelProperty(value = "订单状态变化")
    private List<OrderLogVO> orderLogInfo;

    @ApiModelProperty(value = "商品信息")
    private List<OrderDetailDeliveryVO> orderDetailDelivery;

    @ApiModelProperty(value = "收货人地址信息")
    private OrderAddressVO orderAddress;

    /**
     * 赠品信息
     */
    @ApiModelProperty(value = "赠品信息")
    private List<OrderGiftVO> orderGiftList;



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
}
