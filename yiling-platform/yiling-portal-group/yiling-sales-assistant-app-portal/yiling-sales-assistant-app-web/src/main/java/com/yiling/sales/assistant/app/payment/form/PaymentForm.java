package com.yiling.sales.assistant.app.payment.form;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 支付 Form
 *
 * @author: xuan.zhou
 * @date: 2021/6/29
 */
@Data
public class PaymentForm {

    @NotEmpty
    @ApiModelProperty(value = "订单支付信息列表", required = true)
    private List<OrderPaymentForm> orderPaymentList;

    @Data
    @Valid
    public static class OrderPaymentForm {

        @NotNull
        @Min(1L)
        @ApiModelProperty(value = "订单ID", required = true)
        private Long orderId;

        @NotNull
        @Min(1L)
        @ApiModelProperty(value = "支付方式ID", required = true)
        private Long paymentMethodId;

        @Length(max = 200)
        @ApiModelProperty(value = "买家留言")
        private String buyerMessage;
    }
}
