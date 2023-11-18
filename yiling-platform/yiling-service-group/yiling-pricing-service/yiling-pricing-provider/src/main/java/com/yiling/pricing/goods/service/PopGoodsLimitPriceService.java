package com.yiling.pricing.goods.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.pricing.goods.dto.PopGoodsLimitPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryPopLimitPriceRequest;
import com.yiling.pricing.goods.dto.request.SaveOrUpdatePopLimitPriceRequest;
import com.yiling.pricing.goods.entity.PopGoodsLimitPriceDO;

/**
 * @author shichen
 * @类名 PopGoodsLimitPriceService
 * @描述
 * @创建时间 2023/1/4
 * @修改人 shichen
 * @修改时间 2023/1/4
 **/
public interface PopGoodsLimitPriceService extends BaseService<PopGoodsLimitPriceDO> {
    /**
     * 保存或修改限价信息
     * @param request
     * @return
     */
    Long saveOrUpdate(SaveOrUpdatePopLimitPriceRequest request);

    /**
     * 商品id获取限价
     * @param specificationsId
     * @param status
     * @return
     */
    PopGoodsLimitPriceDTO getLimitPriceBySpecificationsId(Long specificationsId, Integer status);

    /**
     * 批量查询限价
     * @param request
     * @return
     */
    List<PopGoodsLimitPriceDTO> batchQueryLimitPrice(QueryPopLimitPriceRequest request);
}
