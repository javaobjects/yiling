package com.yiling.payment.channel.service.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.channel.service.dto
 * @date: 2021/10/23
 */
@Data
@Accessors(chain=true)
public class QueryPayOrderRequest extends BaseRequest {

    /**
     * 易宝交易商户号
     */
    private String  merchantNo;

    /**
     * 支付流水号
     */
    private String payNo;

    /**
     * 支付第三方交易单号
     */
    private String thirdTradeNo;

    /**
     * 第三方退款单号
     */
    private String third_fund_no;

    /**
     * 退款编号
     */
    private String refund_no;

}
