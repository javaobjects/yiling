package com.yiling.goods.medicine.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.goods.BaseTest;
import com.yiling.goods.medicine.entity.GoodsDiscountRateDO;

import cn.hutool.core.collection.ListUtil;

/**
 * @author: lun.yu
 * @date: 2021/7/21
 */
public class GoodsDiscountRateServiceTest extends BaseTest {


    @Autowired
    private GoodsDiscountRateService goodsDiscountRateService;

    @Test
    public void queryGoodsDiscountRateMap() {
		List<GoodsDiscountRateDO> goodsDiscountRateDOList = goodsDiscountRateService.queryGoodsDiscountRateList(1L, ListUtil.toList(1L, 2L, 3L, 5L, 9L));
		Map<Long, BigDecimal> collect = goodsDiscountRateDOList.stream().collect(Collectors.toMap(GoodsDiscountRateDO::getGoodsId, GoodsDiscountRateDO::getMinDiscountRate, (k1, k2) -> k2));
		collect.forEach((k,v)-> System.out.println(k+"--"+v));
	}
}
