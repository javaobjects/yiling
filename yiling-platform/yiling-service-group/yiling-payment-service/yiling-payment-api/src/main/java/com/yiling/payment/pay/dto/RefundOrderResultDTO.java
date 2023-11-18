package com.yiling.payment.pay.dto;

import java.math.BigDecimal;

import com.yiling.framework.common.base.BaseDTO;
import com.yiling.payment.enums.OrderPlatformEnum;
import com.yiling.payment.enums.RefundStateEnum;
import com.yiling.payment.enums.TradeTypeEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.channel.service.dto
 * @date: 2021/10/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain=true)
public class RefundOrderResultDTO extends BaseDTO {

    /**
     * 第三方接口返回的表示状态
     */
    private String refundThirdState;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 订单编号
     */
    private String appOrderNo;

    /**
     * 订单Id
     */
    private Long appOrderId;

    /**
     * 微信退款Id
     */
    private String refundId;

    /**
     * 应用退款ID
     */
    private Long appRefundId;

    /**
     * 实际退款金额
     */
    private BigDecimal realReturnAmount;

    /**
     * 退款状态
     */
    private RefundStateEnum refundStateEnum;

    /**
     * 交易类型
     */
    private TradeTypeEnum tradeTypeEnum;
    /**
     * 交易平台
     */
    private OrderPlatformEnum orderPlatform;

    /**
     * 支付交易流水号
     */
    private String thirdTradeNo;

    /**
     * 退款交易流水号
     */
    private String thirdFundNo;

    /**
     * 银行交易流水号(微信或者支付宝交易流水号)
     */
    private String bankNo;

    /**
     * 操作人ID
     */
    private Long opUserId;

}
