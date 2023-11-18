package com.yiling.goods.restriction.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.goods.restriction.api.GoodsPurchaseRestrictionApi;
import com.yiling.goods.restriction.dto.GoodsPurchaseRestrictionCustomerDTO;
import com.yiling.goods.restriction.dto.GoodsPurchaseRestrictionDTO;
import com.yiling.goods.restriction.dto.request.DeleteRestrictionCustomerRequest;
import com.yiling.goods.restriction.dto.request.QueryGoodsPurchaseRestrictionRequest;
import com.yiling.goods.restriction.dto.request.QueryRestrictionCustomerRequest;
import com.yiling.goods.restriction.dto.request.SavePurchaseRestrictionRequest;
import com.yiling.goods.restriction.dto.request.SaveRestrictionCustomerRequest;
import com.yiling.goods.restriction.service.GoodsPurchaseRestrictionCustomerService;
import com.yiling.goods.restriction.service.GoodsPurchaseRestrictionService;

/**
 * @author shichen
 * @类名 GoodsPurchaseRestrictionApiImpl
 * @描述
 * @创建时间 2022/12/6
 * @修改人 shichen
 * @修改时间 2022/12/6
 **/
@DubboService
public class GoodsPurchaseRestrictionApiImpl implements GoodsPurchaseRestrictionApi {
    @Autowired
    private GoodsPurchaseRestrictionService goodsPurchaseRestrictionService;

    @Autowired
    private GoodsPurchaseRestrictionCustomerService goodsPurchaseRestrictionCustomerService;

    @Override
    public List<GoodsPurchaseRestrictionDTO> queryValidPurchaseRestriction(QueryGoodsPurchaseRestrictionRequest request) {
        return goodsPurchaseRestrictionService.queryValidPurchaseRestriction(request);
    }

    @Override
    public GoodsPurchaseRestrictionDTO getValidPurchaseRestriction(QueryGoodsPurchaseRestrictionRequest request) {
        return goodsPurchaseRestrictionService.getValidPurchaseRestriction(request);
    }

    @Override
    public List<GoodsPurchaseRestrictionDTO> getPurchaseRestrictionByGoodsIds(List<Long> goodsIds) {
        return goodsPurchaseRestrictionService.getPurchaseRestrictionByGoodsIds(goodsIds);
    }

    @Override
    public Boolean saveGoodsPurchaseRestriction(SavePurchaseRestrictionRequest request) {
        return goodsPurchaseRestrictionService.saveGoodsPurchaseRestriction(request);
    }

    @Override
    public GoodsPurchaseRestrictionDTO getRestrictionByGoodsId(Long goodsId) {
        return goodsPurchaseRestrictionService.getRestrictionByGoodsId(goodsId);
    }

    @Override
    public List<Long> getCustomerEidByGoodsId(Long goodsId) {
        return goodsPurchaseRestrictionCustomerService.getCustomerEidByGoodsId(goodsId);
    }

    @Override
    public Boolean batchSaveRestrictionCustomer(SaveRestrictionCustomerRequest request) {
        return goodsPurchaseRestrictionCustomerService.batchSaveRestrictionCustomer(request);
    }

    @Override
    public Boolean saveRestrictionCustomer(SaveRestrictionCustomerRequest request) {
        return goodsPurchaseRestrictionCustomerService.saveRestrictionCustomer(request);
    }

    @Override
    public List<GoodsPurchaseRestrictionCustomerDTO> queryRestrictionCustomer(QueryRestrictionCustomerRequest request) {
        return goodsPurchaseRestrictionCustomerService.getCustomerByGoodsIdAndCustomerEids(request);
    }

    @Override
    public int batchDeleteRestrictionCustomer(DeleteRestrictionCustomerRequest request) {
        return goodsPurchaseRestrictionCustomerService.batchDeleteByCustomerEids(request);
    }

    @Override
    public int deleteRestrictionCustomer(DeleteRestrictionCustomerRequest request) {
        return goodsPurchaseRestrictionCustomerService.deleteCustomer(request);
    }
}
