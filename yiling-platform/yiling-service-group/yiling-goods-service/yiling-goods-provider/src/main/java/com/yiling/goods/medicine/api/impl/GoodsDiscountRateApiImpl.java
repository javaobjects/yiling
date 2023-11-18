package com.yiling.goods.medicine.api.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.goods.medicine.api.GoodsDiscountRateApi;
import com.yiling.goods.medicine.entity.GoodsDiscountRateDO;
import com.yiling.goods.medicine.service.GoodsDiscountRateService;

import lombok.extern.slf4j.Slf4j;

/**
 * 商品最低折扣比率api实现
 * @author: lun.yu
 * @date: 2021/7/21
 */
@DubboService
@Slf4j
public class GoodsDiscountRateApiImpl implements GoodsDiscountRateApi {

	@Autowired
	private GoodsDiscountRateService goodsDiscountRateService;

	/**
	 * 根据客户id和商品id集合查询商品id对应的最低折扣比率
	 * @param customerEid 客户ID
	 * @param goodsIdList 商品id集合
	 * @return 商品id对应的最低折扣比率
	 */
	@Override
	public Map<Long, BigDecimal> queryGoodsDiscountRateMap(Long customerEid, List<Long> goodsIdList) {
		List<GoodsDiscountRateDO> goodsDiscountRateDOList = goodsDiscountRateService.queryGoodsDiscountRateList(customerEid, goodsIdList);
		return goodsDiscountRateDOList.stream().collect(Collectors.toMap(GoodsDiscountRateDO::getGoodsId,GoodsDiscountRateDO::getMinDiscountRate,(k1,k2)->k2));
	}
}
