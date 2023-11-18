package com.yiling.payment.channel.service.dto.request;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

/** 退款申请
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.channel.service.dto
 * @date: 2021/10/20
 */

@Data
@Accessors(chain=true)
public class CreateRefundRequest {

    /**
     * 交易商户号
     */
    private String merchantNo;

    /**
     * 交易ID
     */
    private String payId;

    /**
     * 交易流水号
     */
    private String payNo;

    /**
     * 第三方交易流水号
     */
    private String thirdTradeNo;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 支付方式
     */
    private String payWay;

    /**
     * 退款流水号
     */
    private String refundNo;

    /**
     * 退款原因
     */
    private String reason;
    /**
     * 对账备注
     */
    private String remark;

}
