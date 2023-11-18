package com.yiling.hmc.order.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.hmc.order.api.MarketOrderAddressApi;
import com.yiling.hmc.order.dto.MarketOrderAddressDTO;
import com.yiling.hmc.order.service.MarketOrderAddressService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/20
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketOrderAddressApiImpl implements MarketOrderAddressApi {

    private final MarketOrderAddressService marketOrderAddressService;

    @Override
    public MarketOrderAddressDTO getAddressByOrderId(Long orderId) {
        return marketOrderAddressService.getAddressByOrderId(orderId);
    }
}
