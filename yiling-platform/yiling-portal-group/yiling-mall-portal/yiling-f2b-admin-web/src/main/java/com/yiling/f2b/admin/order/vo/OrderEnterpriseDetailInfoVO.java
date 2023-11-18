package com.yiling.f2b.admin.order.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;
import com.yiling.framework.common.pojo.vo.FileInfoVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商家后台企业订单详情信息
 * @author:wei.wang
 * @date:2023/04/04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderEnterpriseDetailInfoVO extends BaseVO {

    private Long distributorEid;

    private Long buyerEid;

    @ApiModelProperty(value = "收货地址信息")
    private OrderAddressVO orderAddress;

    @ApiModelProperty(value = "供应商信息")
    private EnterpriseInfoVO enterpriseDistributorInfo;

    @ApiModelProperty(value = "采购商信息")
    private EnterpriseInfoVO enterpriseBuyerInfo;

    @ApiModelProperty(value = "发票信息")
    private OrderInvoiceApplyAllInfoVO orderInvoiceApplyAllInfo;

    @ApiModelProperty(value = "订单状态变化")
    private List<OrderLogVO> orderLogInfo;

    @ApiModelProperty(value = "商品信息")
    private List<OrderDetailVO> orderDetailDelivery;


    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNo;

    /**
     *下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private Date createTime;

    /**
     * 商品总金额
     */
    @ApiModelProperty(value = "成交价总金额")
    private BigDecimal totalAmount;

    /**
     * 商品总金额
     */
    @ApiModelProperty(value = "原价总金额")
    private BigDecimal originalTotalAmount;

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
     * 票折单号
     */
    @ApiModelProperty(value = "票折单号")
    private String ticketDiscountNo;

    /**
     * 票折金额
     */
    @ApiModelProperty(value = "票折金额")
    private BigDecimal ticketDiscountAmount;

    /**
     *优惠金额
     */
    @ApiModelProperty(value = "优惠金额")
    private BigDecimal discountAmount;

    /**
     * 订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消
     */
    @ApiModelProperty(value = "订单状态：10-待审核 20-待发货 30-已发货 40-已收货 100-已完成 -10-已取消")
    private Integer orderStatus;

    @ApiModelProperty(value = "下单备注")
    private String orderNote;

    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    @ApiModelProperty(value = "订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台")
    private Integer orderSource;


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

    /**
     * 物流类型：1-自有物流 2-第三方物流
     */
    @ApiModelProperty(value = "物流类型：1-自有物流 2-第三方物流")
    private Integer deliveryType;


}
