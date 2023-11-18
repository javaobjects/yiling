package com.yiling.mall.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2022/3/1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PopOrderConfirmRequest extends BaseRequest {

    /**
     * 订单支付信息列表
     */
    private List<DistributorOrderDTO> orderPaymentList;

    @Data
    public static class DistributorOrderDTO implements java.io.Serializable{

        /**
         * 订单ID
         */
        private Long orderId;

        /**
         * 支付方式
         */
        private Long paymentMethodId;

        /**
         * 买家留言
         */
        private String buyerMessage;
    }
}
