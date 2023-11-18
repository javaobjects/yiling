package com.yiling.marketing.integral.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.marketing.integral.api.IntegralExchangeGoodsApi;
import com.yiling.marketing.integral.bo.ExchangeGoodsDetailBO;
import com.yiling.marketing.integral.bo.IntegralExchangeGoodsDetailBO;
import com.yiling.marketing.integral.dto.request.RecentExchangeGoodsRequest;
import com.yiling.marketing.integral.service.IntegralExchangeGoodsService;

import lombok.extern.slf4j.Slf4j;

/**
 * 积分兑换商品 API 实现
 *
 * @author: lun.yu
 * @date: 2023-01-09
 */
@Slf4j
@DubboService
public class IntegralExchangeGoodsApiImpl implements IntegralExchangeGoodsApi {

    @Autowired
    IntegralExchangeGoodsService integralExchangeGoodsService;

    @Override
    public IntegralExchangeGoodsDetailBO getDetail(Long id) {
        return integralExchangeGoodsService.getDetail(id);
    }

    @Override
    public ExchangeGoodsDetailBO getGoodsDetail(Long id) {
        return integralExchangeGoodsService.getGoodsDetail(id);
    }

    @Override
    public boolean exchange(RecentExchangeGoodsRequest request) {
        return integralExchangeGoodsService.exchange(request);
    }
}
