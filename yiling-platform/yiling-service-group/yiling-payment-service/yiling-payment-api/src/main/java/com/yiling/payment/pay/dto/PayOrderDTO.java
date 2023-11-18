package com.yiling.payment.pay.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.dto
 * @date: 2021/10/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PayOrderDTO extends BaseDTO {

    private static final long serialVersionUID=6094250982638974120L;
    /**
     * 交易ID
     */
    private String payId;

    /**
     * 商户支付流水号
     */
    private String payNo;

    /**
     * 卖家Eid
     */
    private Long sellerEid;

    /**
     * 买家Eid
     */
    private Long buyerEid;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单ID
     */
    private Long appOrderId;

    /**
     * 订单号
     */
    private String appOrderNo;

    /**
     * 支付订单状态(1:未付款,2:支付交易中，3:支付成功,4:交易取消)
     */
    private Integer appOrderStatus;

    /**
     * 支付失效时间
     */
    private Date limitTime;

    /**
     * 支付成功时间
     */
    private Date payDate;

    /**
     * 支付方式
     */
    private String payWay;

    /**
     * 支付来源:app,pc
     */
    private String paySource;

    /**
     * 交易类型 {@link com.yiling.payment.enums.TradeTypeEnum}
     *
     */
    private Integer tradeType;

    /**
     * 订单平台
     */
    private String orderPlatform;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 货款金额
     */
    private BigDecimal goodsAmount;

    /**
     * '退款状态(1:未退款,2:部分退款,3:已退款)'
     */
    private Integer refundState;

    /**
     * 已退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 支付交易流水号
     */
    private String thirdTradeNo;

    /**
     * 渠道交易单号
     */
    private String bankNo;

    /**
     * 交易描述
     */
    private String content;


}
