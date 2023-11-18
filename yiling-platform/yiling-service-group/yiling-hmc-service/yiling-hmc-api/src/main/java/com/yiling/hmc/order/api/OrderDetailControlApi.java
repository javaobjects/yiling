package com.yiling.hmc.order.api;

import java.util.List;

import com.yiling.hmc.order.bo.OrderDetailControlBO;
import com.yiling.hmc.order.dto.OrderDetailControlDTO;

/**
 * @author: yong.zhang
 * @date: 2022/5/18
 */
public interface OrderDetailControlApi {

    /**
     * 根据订单id和规格id查询订单明细管控信息
     *
     * @param orderId 订单id
     * @param sellSpecificationsId 商品规格id
     * @return 订单明细管控信息
     */
    List<OrderDetailControlDTO> listByOrderIdAndSellSpecificationsId(Long orderId, Long sellSpecificationsId);

    /**
     * 根据订单id和规格id查询订单明细管控信息
     *
     * @param orderId 订单id
     * @param sellSpecificationsIdList 商品规格id
     * @return 管控渠道信息
     */
    List<OrderDetailControlBO> listByOrderIdAndSellSpecificationsIdList(Long orderId, List<Long> sellSpecificationsIdList);
}
