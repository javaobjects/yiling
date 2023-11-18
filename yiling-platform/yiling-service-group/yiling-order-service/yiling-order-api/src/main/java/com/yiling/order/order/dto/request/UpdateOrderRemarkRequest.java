package com.yiling.order.order.dto.request;

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
public class UpdateOrderRemarkRequest extends BaseRequest {

    private List<OrderRemarkRequest> remarkRequests;

    @Data
    public static  class OrderRemarkRequest extends BaseRequest{

        /**
         * 订单ID
         */
        private Long orderId;

        /**
         * 订单备注
         */
        private String orderRemark;

    }
}
