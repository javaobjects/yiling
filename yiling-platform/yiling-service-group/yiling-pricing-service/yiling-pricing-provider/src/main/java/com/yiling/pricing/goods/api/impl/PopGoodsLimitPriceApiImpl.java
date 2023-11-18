package com.yiling.pricing.goods.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.pricing.goods.api.PopGoodsLimitPriceApi;
import com.yiling.pricing.goods.dto.PopGoodsLimitPriceDTO;
import com.yiling.pricing.goods.dto.request.QueryPopLimitPriceRequest;
import com.yiling.pricing.goods.dto.request.SaveOrUpdatePopLimitPriceRequest;
import com.yiling.pricing.goods.service.PopGoodsLimitPriceService;

/**
 * @author shichen
 * @类名 PopGoodsLimitPriceApiImpl
 * @描述
 * @创建时间 2023/1/4
 * @修改人 shichen
 * @修改时间 2023/1/4
 **/
@DubboService
public class PopGoodsLimitPriceApiImpl implements PopGoodsLimitPriceApi {
    @Autowired
    private PopGoodsLimitPriceService popGoodsLimitPriceService;

    @Override
    public Long saveOrUpdate(SaveOrUpdatePopLimitPriceRequest request) {
        return popGoodsLimitPriceService.saveOrUpdate(request);
    }

    @Override
    public PopGoodsLimitPriceDTO getLimitPriceBySpecificationsId(Long specificationsId, Integer status) {
        return popGoodsLimitPriceService.getLimitPriceBySpecificationsId(specificationsId,status);
    }

    @Override
    public List<PopGoodsLimitPriceDTO> batchQueryLimitPrice(QueryPopLimitPriceRequest request) {
        return popGoodsLimitPriceService.batchQueryLimitPrice(request);
    }
}
