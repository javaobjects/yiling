package com.yiling.order.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务单据号枚举
 *
 * @author: xuan.zhou
 * @date: 2021/6/25
 */
@Getter
@AllArgsConstructor
public enum NoEnum {

    /**
     * 订单号
     */
    ORDER_NO("D", MiddelPartMode.DATESTR, 6),

    /**
     * 订单批次号
     */
    ORDER_BATCH_NO("P", MiddelPartMode.DATESTR, 6),

    /**
     * 订单退货单号
     */
    ORDER_RETURN_NO("T", MiddelPartMode.DATESTR, 6),

    /**
     * 返利申请单号号
     */
    AGREEMENT_APPLY_NO("F", MiddelPartMode.DATESTR, 6),

    /**
     * b2b促销结算单号
     */
    B2B_SETTLEMENT_SALE_NO("S", MiddelPartMode.DATESTR, 6),

    /**
     * b2b货款结算单号
     */
    B2B_SETTLEMENT_GOODS_NO("GM", MiddelPartMode.DATESTR, 6),

    /**
     * b2b预售违约结算单号
     */
    B2B_SETTLEMENT_PRESALE_DEFAULT_NO("PD", MiddelPartMode.DATESTR, 6),

    /**
     * 订单开票关联出库单单号
     */
    ORDER_INVOICE_GROUP_DELIVERY_NO("G", MiddelPartMode.DATESTR, 6),

    /**
     * 支付交易流水号
     */
    PAYMENT_TRADE_NO("PT", MiddelPartMode.DATESTR, 6),
    /**
     * 支付退款交易流水号
     */
    PAYMENT_REFUND_NO("PR", MiddelPartMode.DATESTR, 6),
    /**
     * 会员订单号
     */
    MEMBER_ORDER_NO("V", MiddelPartMode.DATESTR, 6),
    /**
     * 退款单号
     */
    ORDER_REFUND_NO("FU", MiddelPartMode.DATESTR, 6),
    /**
     * 积分兑换订单号
     */
    INTEGRAL_ORDER_NO("JF", MiddelPartMode.DATESTR, 6),
    ;

    /**
     * 单据号前缀
     */
    private String prefix;

    /**
     * 中间部分生成方式
     */
    private MiddelPartMode middelPartMode;

    /**
     * 随机数位数
     */
    private Integer randomNum;

    /**
     * 中间部分生成方式
     */
    public enum MiddelPartMode {

        /**
         * 随机数方式
         */
        RANDOM,

        /**
         * 日期字符串方式
         */
        DATESTR
    }
}
