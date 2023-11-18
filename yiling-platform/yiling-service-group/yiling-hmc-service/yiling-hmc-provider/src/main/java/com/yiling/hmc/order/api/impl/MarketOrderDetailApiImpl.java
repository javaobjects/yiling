package com.yiling.hmc.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.hmc.order.api.MarketOrderDetailApi;
import com.yiling.hmc.order.dto.MarketOrderDetailDTO;
import com.yiling.hmc.order.service.MarketOrderDetailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: hongyang.zhang
 * @data: 2023/02/20
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketOrderDetailApiImpl implements MarketOrderDetailApi {

    private final MarketOrderDetailService marketOrderDetailService;


    @Override
    public List<MarketOrderDetailDTO> queryByOrderIdList(List<Long> orderIds) {
        return marketOrderDetailService.queryByOrderIdList(orderIds);
    }

    @Override
    public List<MarketOrderDetailDTO> queryByGoodsNameList(String goodsName) {
        return marketOrderDetailService.queryByGoodsNameList(goodsName);
    }
}
