package com.yiling.pricing.goods.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.pricing.goods.api.GoodsPriceCustomerApi;
import com.yiling.pricing.goods.bo.CountGoodsPriceBO;
import com.yiling.pricing.goods.dto.GoodsPriceCustomerDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceCustomerPageListRequest;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceCustomerRequest;
import com.yiling.pricing.goods.dto.request.SaveOrUpdateGoodsPriceCustomerRequest;
import com.yiling.pricing.goods.entity.GoodsPriceCustomerDO;
import com.yiling.pricing.goods.service.GoodsPriceCustomerService;

/**
 * @author: yuecheng.chen
 * @date: 2021/6/22 0022
 */
@DubboService
public class GoodsPriceCustomerApiImpl implements GoodsPriceCustomerApi {

    @Autowired
    private GoodsPriceCustomerService goodsPriceCustomerService;

    @Override
    public Page<GoodsPriceCustomerDTO> pageList(QueryGoodsPriceCustomerPageListRequest request) {
        Page<GoodsPriceCustomerDO> page = goodsPriceCustomerService.pageList(request);
        return PojoUtils.map(page, GoodsPriceCustomerDTO.class);
    }

    @Override
    public Long saveOrUpdate(SaveOrUpdateGoodsPriceCustomerRequest request) {
        return goodsPriceCustomerService.saveOrUpdate(request);
    }

    @Override
    public Boolean removeById(Long id) {
        return goodsPriceCustomerService.removeById(id);
    }

    @Override
    public GoodsPriceCustomerDTO getById(Long id) {
        return PojoUtils.map(goodsPriceCustomerService.getById(id), GoodsPriceCustomerDTO.class);
    }

    @Override
    public List<CountGoodsPriceBO> countGoodsCustomerPrice( List<Long> goodsIds,Integer goodsLine) {
        return goodsPriceCustomerService.countGoodsCustomerPrice(goodsIds,goodsLine);
    }

    @Override
    public GoodsPriceCustomerDTO get(QueryGoodsPriceCustomerRequest request) {
        return PojoUtils.map(goodsPriceCustomerService.get(request),GoodsPriceCustomerDTO.class);
    }

    @Override
    public List<GoodsPriceCustomerDTO> getByGoodsId(Long goodsId) {
        return goodsPriceCustomerService.getByGoodsId(goodsId);
    }
}
