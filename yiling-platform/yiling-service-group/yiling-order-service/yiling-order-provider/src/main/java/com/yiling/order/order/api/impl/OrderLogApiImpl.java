package com.yiling.order.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.order.order.api.OrderLogApi;
import com.yiling.order.order.dto.OrderLogDTO;
import com.yiling.order.order.service.OrderLogService;

/**
 * 订单日志api
 * @author:wei.wang
 * @date:2021/6/22
 */
@DubboService
public class OrderLogApiImpl implements OrderLogApi {
    @Autowired
    private OrderLogService orderLogService;
    /**
     * 获取日志信息
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderLogDTO> getOrderLogInfo(Long orderId) {
        return orderLogService.getOrderLogInfo(orderId);
    }

    @Override
    public List<OrderLogDTO> getOrderLogInfo(Long orderId, Integer logType) {
        return orderLogService.getOrderLogInfo(orderId,logType);
    }
}
