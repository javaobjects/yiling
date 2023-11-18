package com.yiling.mall.payment.dto.request;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付 Request
 *
 * @author: xuan.zhou
 * @date: 2021/6/29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PaymentRequest extends BaseRequest {

    /**
     * 订单支付信息列表
     */
    @NotEmpty
    private List<OrderPaymentRequest> orderPaymentList;

    /**
     * 订单类型
     */
    @NotEmpty
    private Integer orderType;

    @Data
    public static class OrderPaymentRequest implements java.io.Serializable {

        private static final long serialVersionUID = 8220945161721808530L;

        /**
         * 订单ID
         */
        @NotNull
        @Min(1L)
        private Long orderId;

        /**
         * 支付方式ID
         */
        @NotNull
        @Min(1L)
        private Long paymentMethodId;
    }
}
