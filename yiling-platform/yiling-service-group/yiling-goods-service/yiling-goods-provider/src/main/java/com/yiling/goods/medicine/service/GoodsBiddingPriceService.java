package com.yiling.goods.medicine.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.medicine.dto.GoodsBiddingPriceDTO;
import com.yiling.goods.medicine.dto.request.AddGoodsBiddingPriceRequest;
import com.yiling.goods.medicine.dto.request.GoodsBiddingPriceRequest;
import com.yiling.goods.medicine.entity.GoodsBiddingPriceDO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-07
 */
public interface GoodsBiddingPriceService extends BaseService<GoodsBiddingPriceDO> {

	/**
	 * 根据以岭商品id查询 各省份的招标挂网价（没有设置价格的省份不返回）
	 *
	 * @param goodsIds
	 * @param locationCode
	 * @return
	 */
	List<GoodsBiddingPriceDTO> queryGoodsBidingPriceList(List<Long> goodsIds, String locationCode);

	/**
	 * 批量保存招标挂网价（如 某一药品在某一省份已经设置了招标挂网价则替换）
	 *
	 * @param requests
	 * @return
	 */
	Boolean batchSaveGoodsBiddingPrice(List<GoodsBiddingPriceRequest> requests);

	/**
	 * 保存或更新招标挂网价格
	 *
	 * @param request
	 * @return
	 */
	GoodsBiddingPriceDTO saveOrUpdate(AddGoodsBiddingPriceRequest request);

	/**
	 * 根据商品id和地区编码查询招标挂网价
	 *
	 * @param locationCode
	 * @param goodsId
	 * @return
	 */
	GoodsBiddingPriceDTO queryGoodsBiddingPrice(String locationCode, Long goodsId);

}
