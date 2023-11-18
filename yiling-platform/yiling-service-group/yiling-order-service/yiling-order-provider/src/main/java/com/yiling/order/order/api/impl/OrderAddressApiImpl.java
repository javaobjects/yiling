package com.yiling.order.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.dto.OrderAddressDTO;
import com.yiling.order.order.entity.OrderAddressDO;
import com.yiling.order.order.service.OrderAddressService;

/**
 * 发货地址查询api
 * @author:wei.wang
 * @date:2021/6/22
 */
@DubboService
public class OrderAddressApiImpl implements OrderAddressApi {
    @Autowired
    private OrderAddressService orderAddressService;

    /**
     * 根据orderId获取地址信息
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderAddressDTO getOrderAddressInfo(Long orderId) {
        return orderAddressService.getOrderAddressInfo(orderId);
    }

    /**
     * 根据orderId获取地址信息
     *
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderAddressDTO> getOrderAddressList(List<Long> orderIds) {
        List<OrderAddressDO> orderAddressList = orderAddressService.getOrderAddressList(orderIds);
        return PojoUtils.map(orderAddressList,OrderAddressDTO.class);
    }
}
