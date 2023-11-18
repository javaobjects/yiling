package com.yiling.user.payment.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.enums.PlatformEnum;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 退还账期额度 Request
 *
 * @author: xuan.zhou
 * @date: 2021/7/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RefundPaymentDaysAmountRequest extends BaseRequest {

    /**
     * 订单ID
     */
    private Long       orderId;

    /**
     * 退还额度
     */
    private BigDecimal refundAmount;

    /**
     * 退货单号
     */
    private String returnNo;

    /**
     * 平台类型
     */
    private PlatformEnum platformEnum;
}
