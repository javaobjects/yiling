package com.yiling.export.export.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 退货单导出
 *
 * @author: yong.zhang
 * @date: 2021/11/11
 */
@Data
public class ExportRefundBO {

    /**
     * 退款单号
     */
    private String refundNo;

    /**
     * 单据类型
     */
    private String refundType;

    /**
     * 相关单号
     */
    private String returnNo;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 支付平台
     */
    private String payChannel;

    /**
     * 支付交易流水号
     */
    private String thirdTradeNo;

    /**
     * 退款交易流水号
     */
    private String thirdFundNo;

    /**
     * 退款状态
     */
    private String refundStatus;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 退款时间
     */
    private String refundTime;

    /**
     * 采购商
     */
    private String buyerEname;

    /**
     * 供应商
     */
    private String sellerEname;

    /**
     * 退款总金额
     */
    private BigDecimal refundAmount;

    /**
     * 平台承担券退款金额
     */
    private BigDecimal platformCouponDiscountAmount;

    /**
     * 商家承担券退款金额
     */
    private BigDecimal couponDiscountAmount;

    /**
     * 货款退款金额
     */
    private BigDecimal returnAmount;
}
