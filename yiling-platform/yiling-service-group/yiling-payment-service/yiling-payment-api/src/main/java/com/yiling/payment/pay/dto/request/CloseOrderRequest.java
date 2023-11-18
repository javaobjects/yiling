package com.yiling.payment.pay.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/** 关闭支付交易记录请求
 * @author zhigang.guo
 * @date: 2023/2/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CloseOrderRequest extends BaseRequest {
    private static final long     serialVersionUID = 1L;

    /**
     * 订单来源
     */
    private String orderPlatform;

    /**
     * 订单编号
     */
    private String appOrderNo;
}
