package com.yiling.order.order.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 退款完成回调操作
 *
 * @author: yong.zhang
 * @date: 2021/10/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RefundFinishRequest extends BaseRequest {
    /**
     * 订单ID
     */
    private Long appOrderId;

    /**
     * 订单系统退款单退款ID
     */
    private Long refundId;

    /**
     * 退款状态1-待退款 2-退款中 3-退款成功 4-退款失败
     */
    private Integer refundStatus;

    /**
     * 实际退款金额
     */
    private BigDecimal realRefundAmount;

    /**
     * 退款失败原因
     */
    private String failReason;

    /**
     * 支付交易流水号
     */
    private String thirdTradeNo;

    /**
     * 退款交易流水号
     */
    private String thirdFundNo;


}
