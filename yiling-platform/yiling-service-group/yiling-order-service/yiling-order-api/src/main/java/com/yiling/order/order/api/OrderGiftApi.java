package com.yiling.order.order.api;

import java.util.List;

import com.yiling.order.order.dto.OrderGiftDTO;

/**
 * 订单赠品Api
 * @author:wei.wang
 * @date:2021/11/8
 */
public interface OrderGiftApi {

    /**
     * 根据订单id查询
     * @param orderId
     * @return
     */
    List<OrderGiftDTO> listByOrderId(Long orderId);


    /**
     * 根据订单ID,活动ID查询赠品信息
     * @param orderId 订单ID
     * @param activityId 活动ID
     * @return
     */
    List<OrderGiftDTO> listByOrderId(Long orderId,Long activityId);

    /**
     * 根据订单ids查询
     * @param orderIds
     * @return
     */
    List<OrderGiftDTO> listByOrderIds(List<Long> orderIds);


}
