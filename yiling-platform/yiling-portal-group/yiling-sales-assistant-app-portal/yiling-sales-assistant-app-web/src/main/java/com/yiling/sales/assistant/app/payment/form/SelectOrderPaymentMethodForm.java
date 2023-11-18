package com.yiling.sales.assistant.app.payment.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 勾选订单支付方式 Form
 *
 * @author: xuan.zhou
 * @date: 2021/7/12
 */
@Data
public class SelectOrderPaymentMethodForm {

    @NotNull
    @Min(1L)
    @ApiModelProperty(value = "订单ID", required = true)
    private Long orderId;

    @NotNull
    @ApiModelProperty(value = "支付方式ID", required = true)
    private Long paymentMethodId;
}
