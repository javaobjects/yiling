package com.yiling.hmc.order.api;

import com.yiling.hmc.order.dto.MarketOrderAddressDTO;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/20
 */
public interface MarketOrderAddressApi {
    /**
     * 根据订单id获取订单收货地址
     *
     * @param orderId
     * @return
     */
    MarketOrderAddressDTO getAddressByOrderId(Long orderId);
}
