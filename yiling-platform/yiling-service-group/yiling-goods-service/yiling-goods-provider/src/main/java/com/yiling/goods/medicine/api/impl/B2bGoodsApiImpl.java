package com.yiling.goods.medicine.api.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.B2bGoodsApi;
import com.yiling.goods.medicine.bo.ChoicenessGoodsBO;
import com.yiling.goods.medicine.bo.EnterpriseGoodsCountBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.dto.B2bGoodsDTO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.request.QueryChoicenessGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.QuerySaleSpecificationPageListRequest;
import com.yiling.goods.medicine.dto.request.UpdateGoodsStatusByEidRequest;
import com.yiling.goods.medicine.service.B2bGoodsService;
import com.yiling.goods.medicine.service.GoodsService;
import com.yiling.goods.medicine.service.GoodsSkuService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/11/4
 */
@DubboService
@Slf4j
public class B2bGoodsApiImpl implements B2bGoodsApi {

    @Autowired
    private B2bGoodsService b2bGoodsService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsSkuService goodsSkuService;

    @Override
    public Map<Long, List<GoodsDTO>> getShopGoodsByEidAndLimit(List<Long> eidList, Integer limit) {
        return b2bGoodsService.getShopGoodsByEidAndLimit(eidList,limit);
    }

    @Override
    public Page<GoodsListItemBO> queryB2bGoodsPageList(QueryGoodsPageListRequest request) {
        return b2bGoodsService.queryB2bGoodsPageList(request);
    }

    @Override
    public List<QueryStatusCountBO> queryB2bStatusCountListByCondition(QueryGoodsPageListRequest request) {
        return b2bGoodsService.queryB2bStatusCountListByCondition(request);
    }

    @Override
    public List<QueryStatusCountBO> queryB2bStatusCountList(List<Long> eidList) {
        return b2bGoodsService.queryB2bStatusCountList(eidList);
    }

    @Override
    public List<GoodsInfoDTO> batchQueryInfo(List<Long> goodsIds) {
        List<B2bGoodsDTO> b2bGoodsDTOList = b2bGoodsService.getB2bGoodsListByGoodsIds(goodsIds);
        Map<Long, B2bGoodsDTO> b2bGoodsDTOMap = b2bGoodsDTOList.stream().collect(Collectors.toMap(B2bGoodsDTO::getGoodsId, Function.identity()));
        List<GoodsDTO> goodsDTOList = goodsService.batchQueryInfo(goodsIds);
        List<GoodsInfoDTO> goodsInfoDTOList=PojoUtils.map(goodsDTOList,GoodsInfoDTO.class);
        goodsInfoDTOList.forEach(e->{
            B2bGoodsDTO b2bGoodsDTO=b2bGoodsDTOMap.get(e.getId());
            e.setOutReason(b2bGoodsDTO.getOutReason());
            e.setGoodsStatus(b2bGoodsDTO.getGoodsStatus());
        });
        return goodsInfoDTOList;
    }

    @Override
    public GoodsInfoDTO queryInfo(Long goodsId) {
        return b2bGoodsService.queryInfo(goodsId);
    }

    @Override
    public List<B2bGoodsDTO> getB2bGoodsListByGoodsIds(List<Long> goodsIds) {
        return b2bGoodsService.getB2bGoodsListByGoodsIds(goodsIds);
    }

    @Override
    public B2bGoodsDTO getB2bGoodsByGoodsId(Long goodsId) {
        B2bGoodsDTO b2bGoodsDTO = PojoUtils.map(b2bGoodsService.getB2bGoodsByGoodsId(goodsId), B2bGoodsDTO.class);
        return b2bGoodsDTO;
    }

    @Override
    public List<GoodsDTO> getB2bGoodsSaleTopLimit(List<Long> eids, Integer limit) {
        return b2bGoodsService.getB2bGoodsSaleTopLimit(eids, limit);
    }

    @Override
    public List<Long> getEidListBySaleGoods(List<Long> eids) {
        return b2bGoodsService.getEidListBySaleGoods(eids);
    }

    @Override
    public Long countB2bGoodsByEids(List<Long> eids) {
        return b2bGoodsService.countB2bGoodsByEids(eids);
    }

    @Override
    public Map<Long, List<Long>> getSellerGoodsIdsBySellSpecificationsIds(List<Long> sellSpecificationsIds, List<Long> eids) {
        return b2bGoodsService.getSellerGoodsIdsBySellSpecificationsIds(sellSpecificationsIds,eids);
    }

    @Override
    public Map<Long, List<Long>> getSellerEidsBySellSpecificationsIds(List<Long> sellSpecificationsIds, List<Long> eids) {
        return b2bGoodsService.getSellerEidsBySellSpecificationsIds(sellSpecificationsIds,eids);
    }

    @Override
    public List<Long> getIdsBySpecificationsIdsAndIncludeEidsAndCustomerEid(List<Long> sellSpecificationsIds, List<Long> includeEids, Long buyerEid) {
        return b2bGoodsService.getIdsBySpecificationsIdsAndIncludeEidsAndCustomerEid(sellSpecificationsIds,includeEids,buyerEid);
    }

    @Override
    public Page<ChoicenessGoodsBO> getChoicenessByCustomerAndSellSpecificationsId(QueryChoicenessGoodsPageListRequest request) {
        return b2bGoodsService.getChoicenessByCustomerAndSellSpecificationsId(request);
    }

    @Override
    public List<Long> getB2bInventoryByGoodsId(Long goodsId) {
        return goodsSkuService.getB2bInventoryByGoodsId(goodsId);
    }

    @Override
    public boolean updateB2bStatusByEid(UpdateGoodsStatusByEidRequest request) {
        return b2bGoodsService.updateB2bStatusByEid(request);
    }

    @Override
    public EnterpriseGoodsCountBO getGoodsCountByEid(Long eid) {
        return b2bGoodsService.getGoodsCountByEid(eid);
    }

    @Override
    public Map<Long, EnterpriseGoodsCountBO> getGoodsCountByEidList(List<Long> eidList) {
        return b2bGoodsService.getGoodsCountByEidList(eidList);
    }

    @Override
    public List<GoodsListItemBO> getB2bGoodsBySellSpecificationsIdsAndEids(QueryGoodsPageListRequest request) {
        return b2bGoodsService.getB2bGoodsBySellSpecificationsIdsAndEids(request);
    }

    @Override
    public Map<Long, BigDecimal> getMinPriceBySpecificationsIds(List<Long> sellSpecificationsIds) {
        return b2bGoodsService.getMinPriceBySpecificationsIds(sellSpecificationsIds);
    }

    @Override
    public Page<GoodsListItemBO> queryWaterfallSpecificationPage(QuerySaleSpecificationPageListRequest request) {
        return b2bGoodsService.queryWaterfallSpecificationPage(request);
    }

    @Override
    public List<Long> getInStockGoodsBySpecId(Long sellSpecificationsId) {
        return b2bGoodsService.getInStockGoodsBySpecId(sellSpecificationsId);
    }

    @Override
    public Page<ChoicenessGoodsBO> queryDistributorGoodsBySpec(QueryChoicenessGoodsPageListRequest request) {
        return b2bGoodsService.queryDistributorGoodsBySpec(request);
    }
}
