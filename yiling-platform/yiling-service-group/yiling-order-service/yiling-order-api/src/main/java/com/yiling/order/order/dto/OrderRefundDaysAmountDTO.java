package com.yiling.order.order.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:wei.wang
 * @date:2021/7/16
 */
@Data
@Accessors(chain = true)
public class OrderRefundDaysAmountDTO {
    /**
     * 订单ID
     */
    private Long       orderId;

    /**
     * 退还额度
     */
    private BigDecimal refundAmount;

    /**
     * 支付方式
     */
    private Integer paymentMethod;
}
