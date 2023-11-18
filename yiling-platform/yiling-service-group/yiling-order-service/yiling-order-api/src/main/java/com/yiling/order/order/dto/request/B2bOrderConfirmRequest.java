package com.yiling.order.order.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto.request
 * @date: 2021/12/22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class B2bOrderConfirmRequest extends CreateOrderRequest {
    private static final long serialVersionUID = -4808238520028878960L;
    /**
     * 订单Id
     */
    private Long orderId;

}
