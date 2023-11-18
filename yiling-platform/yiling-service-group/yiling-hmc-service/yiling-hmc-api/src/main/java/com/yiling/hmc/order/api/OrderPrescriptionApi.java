package com.yiling.hmc.order.api;

import com.yiling.hmc.order.dto.OrderPrescriptionDTO;

/**
 * 订单处方API
 *
 * @author: fan.shen
 * @date: 2022/4/11
 */
public interface OrderPrescriptionApi {

    /**
     * 获取订单处方
     *
     * @param orderId
     */
    OrderPrescriptionDTO getByOrderId(Long orderId);
}
