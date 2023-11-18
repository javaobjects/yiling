package com.yiling.payment.channel.service.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Builder;
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
@Builder
public class ClosePayOrderRequest extends BaseRequest {

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

}