package com.yiling.goods.medicine.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.goods.medicine.api.GoodsLimitPriceApi;
import com.yiling.goods.medicine.bo.CustomerGoodsPriceLimitBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.CustomerPriceLimitDTO;
import com.yiling.goods.medicine.dto.GoodsPriceLimitDTO;
import com.yiling.goods.medicine.dto.request.AddOrDeleteCustomerGoodsLimitRequest;
import com.yiling.goods.medicine.dto.request.BatchAddCustomerGoodsLimitRequest;
import com.yiling.goods.medicine.dto.request.DeleteGoodsPriceLimitRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsLimitPageListRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPriceLimitPageRequest;
import com.yiling.goods.medicine.dto.request.QueryLimitFlagRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsPriceLimitRequest;
import com.yiling.goods.medicine.dto.request.UpdateCustomerLimitRequest;
import com.yiling.goods.medicine.service.CustomerGoodsPriceLimitService;
import com.yiling.goods.medicine.service.CustomerPriceLimitService;
import com.yiling.goods.medicine.service.GoodsPriceLimitService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/10/25
 */
@DubboService
@Slf4j
public class GoodsLimitPriceApiImpl implements GoodsLimitPriceApi {

    @Autowired
    private CustomerPriceLimitService customerPriceLimitService;

    @Autowired
    private CustomerGoodsPriceLimitService customerGoodsPriceLimitService;

    @Autowired
    private GoodsPriceLimitService goodsPriceLimitService;

    @Override
    public List<CustomerPriceLimitDTO> getCustomerLimitFlagByEidAndCustomerEid(QueryLimitFlagRequest request) {
        return customerPriceLimitService.getCustomerLimitFlagByEidAndCustomerEid(request);
    }

    @Override
    public Boolean updateCustomerLimitByEidAndCustomerEid(UpdateCustomerLimitRequest request) {
        return customerPriceLimitService.updateCustomerLimitByEidAndCustomerEid(request);
    }

    @Override
    public Map<Long, Integer> getRecommendationFlagByCustomerEids(List<Long> customerEids,Long eid) {
        return customerPriceLimitService.getRecommendationFlagByCustomerEids(customerEids,eid);
    }

    @Override
    public Boolean addCustomerGoodsLimitByCustomerEid(AddOrDeleteCustomerGoodsLimitRequest request) {
        return customerGoodsPriceLimitService.addCustomerGoodsLimitByCustomerEid(request);
    }

    @Override
    public Boolean batchAddCustomerGoodsLimitByCustomerEid(BatchAddCustomerGoodsLimitRequest request) {
        return customerGoodsPriceLimitService.batchAddCustomerGoodsLimitByCustomerEid(request);
    }

    @Override
    public Boolean deleteCustomerGoodsLimitByCustomerEid(AddOrDeleteCustomerGoodsLimitRequest request) {
        return customerGoodsPriceLimitService.deleteCustomerGoodsLimitByCustomerEid(request);
    }

    @Override
    public Page<GoodsPriceLimitDTO> pageList(QueryGoodsPriceLimitPageRequest request) {
        return goodsPriceLimitService.pageList(request);
    }

    @Override
    public Page<GoodsListItemBO> pageLimitList(QueryGoodsLimitPageListRequest request) {
        return customerGoodsPriceLimitService.pageLimitList(request);
    }

    @Override
    public Boolean addGoodsPriceLimit(SaveOrUpdateGoodsPriceLimitRequest request) {
        return goodsPriceLimitService.addGoodsPriceLimit(request);
    }

    @Override
    public GoodsPriceLimitDTO getGoodsPriceLimitById(Long id) {
        return goodsPriceLimitService.getGoodsPriceLimitById(id);
    }

    @Override
    public Boolean deleteGoodsPriceLimit(DeleteGoodsPriceLimitRequest request) {
        return goodsPriceLimitService.deleteGoodsPriceLimit(request);
    }

    @Override
    public Boolean updateGoodsPriceLimit(SaveOrUpdateGoodsPriceLimitRequest request) {
        return goodsPriceLimitService.updateGoodsPriceLimit(request);
    }

    @Override
    public Map<Long, List<GoodsPriceLimitDTO>> listGoodsPriceLimitByGoodsIds(List<Long> goodsIds) {
        return goodsPriceLimitService.listGoodsPriceLimitByGoodsIds(goodsIds);
    }

    @Override
    public CustomerPriceLimitDTO getCustomerPriceLimitByCustomerEid(Long eid, Long customerEid) {
        return customerPriceLimitService.getCustomerPriceByCustomerEidAndEid(eid,customerEid);
    }

    @Override
    public List<Long> getGoodsIdsByCustomerEid(Long eid,Long customerEid) {
        return customerGoodsPriceLimitService.getGoodsIdsByCustomerEid(eid,customerEid);
    }

    @Override
    public List<Long> getIsGoodsPriceByGoodsIdsAndBuyerEid(List<Long> gidList, Long eid, Long buyerEid) {
        return customerGoodsPriceLimitService.getIsGoodsPriceByGoodsIdsAndBuyerEid(gidList, eid, buyerEid);
    }

    @Override
    public Map<Long,List<Long>> getLimitByGidsAndCustomerEidsGroupByCustomerEid(List<Long> gidList, List<Long> customerEid,Long eid) {
        return customerGoodsPriceLimitService.getLimitByGidsAndCustomerEidsGroupByCustomerEid(gidList,customerEid,eid);
    }
}
