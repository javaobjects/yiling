package com.yiling.hmc.order.api;

import java.util.List;

import com.yiling.hmc.order.dto.OrderDetailDTO;

/**
 * @author: yong.zhang
 * @date: 2022/3/25
 */
public interface OrderDetailApi {

    /**
     * 根据订单明细id查询订单明细
     *
     * @param id 订单明细id
     * @return 订单明细信息
     */
    OrderDetailDTO getById(Long id);

    /**
     * 根据订单id查询订单明细
     *
     * @param orderId 订单id
     * @return 订单明细
     */
    List<OrderDetailDTO> listByOrderId(Long orderId);
}
