package com.yiling.order.order.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * @author zhigang.guo
 * @date: 2022/5/30
 */
@Data
public class OrderFirstInfoDTO implements java.io.Serializable{

    /**
     * 订单Id
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单类型：1-POP订单,2-B2B订单
     */
    private Integer orderType;

    /**
     * 订单来源：1-POP-PC平台,2-POP-APP平台,3-B2B-APP平台,4-销售助手-APP平台
     */
    private Integer orderSource;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 买家企业ID
     */
    private Long buyerEid;

    /**
     * 买家企业ID
     */
    private String buyerEname;

    /**
     * 支付类型：1-线下支付 2-在线支付
     */
    private Integer paymentType;

    /**
     * 支付方式：1-线下支付 2-账期 3-预付款 4-在线支付
     */
    private Integer paymentMethod;

    /**
     * 支付金额
     */
    private BigDecimal paymentAmount;

    /**
     * 订单金额
     */
    private BigDecimal totalAmount;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

}
