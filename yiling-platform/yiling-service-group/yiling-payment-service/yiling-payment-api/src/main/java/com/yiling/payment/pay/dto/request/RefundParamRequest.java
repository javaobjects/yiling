package com.yiling.payment.pay.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 退款参数
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.channel.dto.request
 * @date: 2021/10/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
public class RefundParamRequest extends BaseRequest {

    private static final long serialVersionUID=-3671863510431741522L;
    /**
     * 订单ID
     */
    private Long appOrderId;

    /**
     * 订单编号
     */
    private String appOrderNo;

    /**
     * 订单系统退款单退款ID
     */
    private Long refundId;

    /**
     * 支付交易流水单号
     */
    private String payNo;

    /**
     * 交易类型(1:定金,2:支付,3:在线还款,4:尾款,5:会员 6:问诊 7:处方)
     * {@link com.yiling.payment.enums.TradeTypeEnum}
     */
    private Integer tradeType = 2;

    /**
     * 退款类型：1订单支付退款 ，2为重复退款
     */
    private Integer refundType = 1;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
}
