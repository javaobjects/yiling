package com.yiling.order.order.dto.request;

import java.math.BigDecimal;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateOrderPaymentMethodRequest extends BaseRequest {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 支付方式ID
     */
    private Integer paymentMethodId;

    /**
     * 实付金额
     */
    private BigDecimal paymentAmount;

}
