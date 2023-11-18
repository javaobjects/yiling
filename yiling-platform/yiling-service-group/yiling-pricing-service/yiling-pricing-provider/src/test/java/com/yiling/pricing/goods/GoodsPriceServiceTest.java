package com.yiling.pricing.goods;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.pricing.BaseTest;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceRequest;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: xuan.zhou
 * @date: 2021/7/9
 */
@Slf4j
public class GoodsPriceServiceTest extends BaseTest {

    @Autowired
    private com.yiling.pricing.goods.service.GoodsPriceService goodsPriceService;

    @Test
    public void queryGoodsPriceMap() {
        QueryGoodsPriceRequest request = new QueryGoodsPriceRequest();
        request.setCustomerEid(6L);
        request.setGoodsIds(ListUtil.toList(1L, 2L, 4L));

        Map<Long, BigDecimal> goodsPriceMap = goodsPriceService.queryGoodsPriceMap(request);
        log.info("goodsPriceMap = {}", JSONUtil.toJsonStr(goodsPriceMap));
    }

    @Test
    public void queryGoodsPriceMap2() {
        QueryGoodsPriceRequest request = new QueryGoodsPriceRequest();
        request.setCustomerEid(55L);
        request.setGoodsIds(ListUtil.toList(3378L));

        Map<Long, BigDecimal> goodsPriceMap = goodsPriceService.queryB2bGoodsPriceMap(request);
        log.info("goodsPriceMap = {}", JSONUtil.toJsonStr(goodsPriceMap));
    }
}
