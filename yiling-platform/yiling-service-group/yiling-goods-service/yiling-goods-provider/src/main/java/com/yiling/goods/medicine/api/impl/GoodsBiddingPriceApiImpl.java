package com.yiling.goods.medicine.api.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.goods.medicine.api.GoodsBiddingPriceApi;
import com.yiling.goods.medicine.dto.GoodsBiddingPriceDTO;
import com.yiling.goods.medicine.dto.request.AddGoodsBiddingPriceRequest;
import com.yiling.goods.medicine.dto.request.GoodsBiddingPriceRequest;
import com.yiling.goods.medicine.service.GoodsBiddingPriceService;

/**
 * @author dexi.yao
 * @date 2021-07-07
 */
@DubboService
public class GoodsBiddingPriceApiImpl implements GoodsBiddingPriceApi {

	@Autowired
	GoodsBiddingPriceService goodsBiddingPriceService;

	@Override
	public List<GoodsBiddingPriceDTO> queryGoodsBidingPriceList(List<Long> goodsIds) {
		return goodsBiddingPriceService.queryGoodsBidingPriceList(goodsIds, null);
	}

	@Override
	public Map<Long, BigDecimal> queryGoodsBidingPriceByLocation(List<Long> goodsIds, String locationCode) {
		List<GoodsBiddingPriceDTO> priceDTOList = goodsBiddingPriceService.queryGoodsBidingPriceList(goodsIds, locationCode);
		return priceDTOList.stream().collect(Collectors.toMap(GoodsBiddingPriceDTO::getGoodsId, GoodsBiddingPriceDTO::getPrice));
	}

	@Override
	public Boolean batchSaveGoodsBiddingPrice(List<GoodsBiddingPriceRequest> requests) {
		return goodsBiddingPriceService.batchSaveGoodsBiddingPrice(requests);
	}

	@Override
	public GoodsBiddingPriceDTO saveOrUpdate(AddGoodsBiddingPriceRequest request) {
		return goodsBiddingPriceService.saveOrUpdate(request);
	}

	@Override
	public GoodsBiddingPriceDTO queryGoodsBiddingPrice(String locationCode, Long goodsId) {
		return goodsBiddingPriceService.queryGoodsBiddingPrice(locationCode, goodsId);
	}
}
