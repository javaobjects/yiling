package com.yiling.pricing.goods.api.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.pricing.goods.api.GoodsPriceApi;
import com.yiling.pricing.goods.dto.GoodsPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;
import com.yiling.pricing.goods.service.GoodsPriceService;

/**
 * 商品定价 API
 *
 * @author xuan.zhou
 * @date 2021/7/9
 */
@DubboService
public class GoodsPriceApiImpl implements GoodsPriceApi {

    @Autowired
    private GoodsPriceService goodsPriceService;

    @Override
    public Map<Long, BigDecimal> queryGoodsPriceMap(QueryGoodsPriceRequest request) {
        return goodsPriceService.queryGoodsPriceMap(request);
    }

    @Override
    public Map<Long, GoodsPriceDTO> queryB2bGoodsPriceInfoMap(QueryGoodsPriceRequest request) {
        return goodsPriceService.queryB2bGoodsPriceInfoMap(request);
    }

    @Override
    public Map<Long, BigDecimal> queryB2bGoodsPriceMap(QueryGoodsPriceRequest request) {
        return goodsPriceService.queryB2bGoodsPriceMap(request);
    }

    @Override
    public Map<Long, Boolean> queryB2bGoodsPriceShowMap(QueryGoodsPriceRequest request) {
        return goodsPriceService.queryB2bGoodsPriceShowMap(request);
    }

    @Override
    public Map<Long, Integer> getB2bGoodsPriceLimitByGids(List<Long> gidList, Long eid, Long buyerEid) {
        return goodsPriceService.getB2bGoodsPriceLimitByGids(gidList,eid,buyerEid);
    }
}
