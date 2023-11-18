package com.yiling.export.export.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author: yong.zhang
 * @date: 2022/4/11
 */
@Data
public class HmcAdminOrderBO {

    /**
     * 对应订单号
     */
    private String orderNo;

    /**
     * 第三方兑保编号
     */
    private String thirdConfirmNo;

    /**
     * 对应保司保险单号
     */
    private String policyNo;

    /**
     * 下单时间
     */
    private String orderTime;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 药品服务终端
     */
    private String ename;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 支付时间/确认时间
     */
    private String paymentTime;

    /**
     * 订单额
     */
    private BigDecimal totalAmount;

    /**
     * 用户支付状态
     */
    private String paymentStatus;

    /**
     * 完成时间
     */
    private String finishTime;

    /**
     * 理赔款结算额
     */
    private BigDecimal insuranceSettlementAmount;

    /**
     * 保司结算状态
     */
    private String insuranceSettlementStatus;

    /**
     * 终端结算额
     */
    private BigDecimal terminalSettlementAmount;

    /**
     * 终端结算状态
     */
    private String terminalSettlementStatus;

    /**
     * 配送方式
     */
    private String deliverType;

    /**
     * 商品明细
     */
    private String orderDetail;
}
