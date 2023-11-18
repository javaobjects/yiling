package com.yiling.order.order.dto.request;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @version V1.0
 * @Package com.yiling.order.order.dto.request
 * @date: 2021/9/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class OrderSumQueryRequest extends ReceiveOrderSumQueryRequest {

    private static final long serialVersionUID=7708806573918270883L;

    /**
     * 订单状态集合
     */
    private List<Integer> orderStatusList;





}
