package com.yiling.order.order.api;

import java.util.List;

import com.yiling.order.order.dto.OrderAddressDTO;

/**
 * 发货地址查询api
 * @author:wei.wang
 * @date:2021/6/22
 */
public interface OrderAddressApi {

    /**
     * 根据orderId获取地址信息
     * @param orderId
     * @return
     */
    OrderAddressDTO getOrderAddressInfo(Long orderId);

    /**
     * 根据orderId获取地址信息
     * @param orderIds
     * @return
     */
    List<OrderAddressDTO> getOrderAddressList(List<Long> orderIds);
}
