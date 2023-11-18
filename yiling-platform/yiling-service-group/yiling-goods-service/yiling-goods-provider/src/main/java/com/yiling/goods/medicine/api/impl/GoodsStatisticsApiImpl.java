package com.yiling.goods.medicine.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.goods.medicine.api.GoodsStatisticsApi;
import com.yiling.goods.medicine.dto.request.StatisticsGoodsSaleRequest;
import com.yiling.goods.medicine.service.GoodsStatisticsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/5/20
 */
@DubboService
@Slf4j
public class GoodsStatisticsApiImpl implements GoodsStatisticsApi {

    @Autowired
    GoodsStatisticsService goodsStatisticsService;

    @Override
    public Boolean statisticsGoodsSale(StatisticsGoodsSaleRequest request) {
        return goodsStatisticsService.statisticsGoodsSale(request);
    }
}
