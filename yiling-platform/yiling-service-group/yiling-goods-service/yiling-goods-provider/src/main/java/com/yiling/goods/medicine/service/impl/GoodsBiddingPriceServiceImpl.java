package com.yiling.goods.medicine.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.dao.GoodsBiddingPriceMapper;
import com.yiling.goods.medicine.dto.GoodsBiddingPriceDTO;
import com.yiling.goods.medicine.dto.request.AddGoodsBiddingPriceRequest;
import com.yiling.goods.medicine.dto.request.GoodsBiddingPriceRequest;
import com.yiling.goods.medicine.entity.GoodsBiddingPriceDO;
import com.yiling.goods.medicine.service.GoodsBiddingPriceService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-07
 */
@Service
@Slf4j
public class GoodsBiddingPriceServiceImpl extends BaseServiceImpl<GoodsBiddingPriceMapper, GoodsBiddingPriceDO> implements GoodsBiddingPriceService {

	@Override
	public List<GoodsBiddingPriceDTO> queryGoodsBidingPriceList(List<Long> goodsIds, String locationCode) {
		if (CollUtil.isNotEmpty(goodsIds)) {
			QueryWrapper<GoodsBiddingPriceDO> queryWrapper = new QueryWrapper<>();
			queryWrapper.lambda().gt(GoodsBiddingPriceDO::getPrice, 0).in(GoodsBiddingPriceDO::getGoodsId, goodsIds);
			if (StrUtil.isNotBlank(locationCode)) {
				queryWrapper.lambda().eq(GoodsBiddingPriceDO::getLocationCode, locationCode);
			}
			List<GoodsBiddingPriceDO> biddingPriceList = list(queryWrapper);
			return PojoUtils.map(biddingPriceList, GoodsBiddingPriceDTO.class);

		} else {
			return ListUtil.toList();
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean batchSaveGoodsBiddingPrice(List<GoodsBiddingPriceRequest> requests) {
		//已经设置过价格的商品
		List<GoodsBiddingPriceDTO> dbGoodsPriceList;
		//需要更新价格的商品及其省份
		List<GoodsBiddingPriceDTO> updateGoodsPriceList = ListUtil.toList();

		//根据商品id查询以设置价格的商品
		dbGoodsPriceList = queryGoodsBidingPriceList(requests.stream()
				.map(GoodsBiddingPriceRequest::getGoodsId).collect(Collectors.toList()), null);
		//找出需要更新价格的省份
		{
			if (CollUtil.isNotEmpty(dbGoodsPriceList)) {
				//商品map
				Map<Long, List<GoodsBiddingPriceDTO>> goodsMap;

				goodsMap = dbGoodsPriceList.stream().collect(Collectors.groupingBy(GoodsBiddingPriceDTO::getGoodsId));
				requests.forEach(e -> {
					//某一商品的各省份价格
					List<GoodsBiddingPriceDTO> goodsPrices = goodsMap.get(e.getGoodsId());
					//某一商品的某一省份价格 key=locationCode
					Map<String, List<GoodsBiddingPriceDTO>> dbGoodsLocationPriceMap;
					if (goodsPrices != null) {
						dbGoodsLocationPriceMap = goodsPrices.stream().collect(Collectors.groupingBy(GoodsBiddingPriceDTO::getLocationCode));
						List<GoodsBiddingPriceDTO> locationPriceList = dbGoodsLocationPriceMap.get(e.getLocationCode());
						if (CollUtil.isNotEmpty(locationPriceList)) {
							GoodsBiddingPriceDTO updateGoodsPrice = locationPriceList.get(0);
							if (updateGoodsPrice != null) {
								//放入需要更新价格的省份列表
								updateGoodsPriceList.add(updateGoodsPrice);
							}
						}
					}
				});

			}
		}
		//根据updateGoodsPriceList删除原有的价格
		if (CollUtil.isNotEmpty(updateGoodsPriceList)) {
			QueryWrapper<GoodsBiddingPriceDO> deleteWrapper = new QueryWrapper<>();
			deleteWrapper.lambda().in(GoodsBiddingPriceDO::getId, updateGoodsPriceList.stream()
					.map(GoodsBiddingPriceDTO::getId).collect(Collectors.toList()));
			GoodsBiddingPriceDO goodsBiddingPriceDO = new GoodsBiddingPriceDO();
			goodsBiddingPriceDO.setOpUserId(requests.get(0).getOpUserId());
			int rows = batchDeleteWithFill(goodsBiddingPriceDO, deleteWrapper);
			if (rows != updateGoodsPriceList.size()) {
				log.error("更新招标挂网价失败");
				return false;
			}
		}
		//招标挂网价存盘
		List<GoodsBiddingPriceDO> biddingPriceDOList = PojoUtils.map(requests, GoodsBiddingPriceDO.class);
		return saveBatch(biddingPriceDOList);
	}

	@Override
	public GoodsBiddingPriceDTO saveOrUpdate(AddGoodsBiddingPriceRequest request) {
		GoodsBiddingPriceDO priceDO = PojoUtils.map(request, GoodsBiddingPriceDO.class);
		boolean isSuccess = saveOrUpdate(priceDO);
		if (!isSuccess) {
			throw new BusinessException();
		}
		GoodsBiddingPriceDTO priceDTO = PojoUtils.map(priceDO, GoodsBiddingPriceDTO.class);
		return priceDTO;
	}

	@Override
	public GoodsBiddingPriceDTO queryGoodsBiddingPrice(String locationCode, Long goodsId) {
		Assert.notNull(locationCode, "地区编码不能为空");
		Assert.notNull(goodsId, "商品Id不能为空");
		LambdaQueryWrapper<GoodsBiddingPriceDO> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(GoodsBiddingPriceDO::getLocationCode, locationCode)
				.eq(GoodsBiddingPriceDO::getGoodsId, goodsId);
		GoodsBiddingPriceDO priceDO = getOne(wrapper);
		return PojoUtils.map(priceDO, GoodsBiddingPriceDTO.class);
	}
}
