package com.yiling.payment.pay.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.payment.pay.dto.request
 * @date: 2021/10/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Builder
public class InsertTradeLogRequest extends BaseRequest {
    /**
     * 交易编号
     */
    private String payNo;

    /**
     * 退款交易编号
     */
    private String refundNo;

    /**
     * 日志类型(1,支付回调,2,退款回调,3,打款回调)
     */
    private Integer tradeType;

    /**
     * 同步回调日志记录
     */
    private String syncLog;

    /**
     * 第三方支付方式
     */
    private String payWay;
}
