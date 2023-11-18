package com.yiling.pricing.goods.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.pricing.goods.api.GoodsPriceCustomerGroupApi;
import com.yiling.pricing.goods.bo.CountGoodsPriceBO;
import com.yiling.pricing.goods.dto.GoodsPriceCustomerDTO;
import com.yiling.pricing.goods.dto.GoodsPriceCustomerGroupDTO;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceCustomerGroupPageListRequest;
import com.yiling.pricing.goods.dto.request.QueryGoodsPriceGroupRequest;
import com.yiling.pricing.goods.dto.request.SaveOrUpdateGoodsPriceCustomerGroupRequest;
import com.yiling.pricing.goods.entity.GoodsPriceCustomerGroupDO;
import com.yiling.pricing.goods.service.GoodsPriceCustomerGroupService;

/**
 * @author: yuecheng.chen
 * @date: 2021/6/22 0022
 */
@DubboService
public class GoodsPriceCustomerGroupApiImpl implements GoodsPriceCustomerGroupApi {

    @Autowired
    private GoodsPriceCustomerGroupService goodsPriceCustomerGroupService;

    @Override
    public Page<GoodsPriceCustomerGroupDTO> pageList(QueryGoodsPriceCustomerGroupPageListRequest request) {
        Page<GoodsPriceCustomerGroupDO> page = goodsPriceCustomerGroupService.pageList(request);
        return PojoUtils.map(page, GoodsPriceCustomerGroupDTO.class);
    }

    @Override
    public Long saveOrUpdate(SaveOrUpdateGoodsPriceCustomerGroupRequest request) {
        return goodsPriceCustomerGroupService.saveOrUpdate(request);
    }

    @Override
    public Boolean removeById(Long id) {
        return goodsPriceCustomerGroupService.removeById(id);
    }

    @Override
    public GoodsPriceCustomerDTO getById(Long id) {
        return PojoUtils.map(goodsPriceCustomerGroupService.getById(id), GoodsPriceCustomerDTO.class);
    }

    @Override
    public GoodsPriceCustomerGroupDTO get(QueryGoodsPriceGroupRequest queryGoodsPriceGroupRequest) {
        return PojoUtils.map(goodsPriceCustomerGroupService.get(queryGoodsPriceGroupRequest), GoodsPriceCustomerGroupDTO.class);
    }

    @Override
    public List<CountGoodsPriceBO> countGoodsCustomerGroupPrice(List<Long> goodsIds,Integer goodsLine) {
        return goodsPriceCustomerGroupService.countGoodsCustomerGroupPrice(goodsIds,goodsLine);
    }

    @Override
    public List<GoodsPriceCustomerGroupDTO> getByGoodsId(Long goodsId) {
        return goodsPriceCustomerGroupService.getByGoodsId(goodsId);
    }
}
