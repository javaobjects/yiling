package com.yiling.hmc.order.bo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: yong.zhang
 * @date: 2022/5/18
 */
@Data
@Accessors(chain = true)
public class OrderDetailControlBO implements Serializable {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 售卖规格id
     */
    private Long sellSpecificationsId;

    /**
     * 管控渠道
     */
    private List<Long> eidList;
}
