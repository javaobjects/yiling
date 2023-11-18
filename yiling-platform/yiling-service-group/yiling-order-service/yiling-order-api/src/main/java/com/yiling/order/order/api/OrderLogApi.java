package com.yiling.order.order.api;

import java.util.List;

import com.yiling.order.order.dto.OrderLogDTO;

/**
 * 订单日志api
 * @author:wei.wang
 * @date:2021/6/22
 */
public interface OrderLogApi {
    /**
     * 获取日志信息
     * @param orderId
     * @return
     */
    List<OrderLogDTO> getOrderLogInfo(Long orderId);
    /**
     * 获取日志信息
     * @param orderId
     * @return
     */
    List<OrderLogDTO> getOrderLogInfo(Long orderId,Integer logType);
}
