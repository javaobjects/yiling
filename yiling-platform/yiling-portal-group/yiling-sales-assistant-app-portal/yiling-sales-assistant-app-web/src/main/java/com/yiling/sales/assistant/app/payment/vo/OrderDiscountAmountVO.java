package com.yiling.sales.assistant.app.payment.vo;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 订单折扣金额 VO
 *
 * @author: xuan.zhou
 * @date: 2021/7/12
 */
@Data
@AllArgsConstructor
public class OrderDiscountAmountVO {

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单折扣金额")
    private BigDecimal discountAmount;

    @ApiModelProperty("支付金额")
    private BigDecimal paymentAmount;

}
