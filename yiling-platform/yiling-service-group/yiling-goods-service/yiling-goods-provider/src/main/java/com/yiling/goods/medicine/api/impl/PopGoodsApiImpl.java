package com.yiling.goods.medicine.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.bo.EnterpriseGoodsCountBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.bo.QueryStatusCountBO;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.goods.medicine.dto.PopGoodsDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.enums.GoodsErrorCode;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.medicine.service.PopGoodsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/11/4
 */
@DubboService
@Slf4j
public class PopGoodsApiImpl implements PopGoodsApi {

    @Autowired
    private PopGoodsService popGoodsService;

    @Override
    public Page<GoodsListItemBO> queryPopGoodsPageList(QueryGoodsPageListRequest request) {
        return popGoodsService.queryPopGoodsPageList(request);
    }

    @Override
    public List<GoodsListItemBO> queryPopGoodsList(QueryGoodsPageListRequest request) {
        return popGoodsService.queryPopGoodsList(request);
    }

    @Override
    public List<QueryStatusCountBO> queryPopStatusCountList(List<Long> eidList) {
        return popGoodsService.queryPopStatusCountList(eidList);
    }

    @Override
    public GoodsInfoDTO findGoodsByInSnAndEid(Long eid, String inSn) {
        return popGoodsService.findGoodsByInSnAndEid(eid, inSn);
    }

    @Override
    public GoodsInfoDTO queryInfo(Long goodsId) {
        return popGoodsService.queryInfo(goodsId);
    }

    @Override
    public List<GoodsInfoDTO> batchQueryInfo(List<Long> goodsIds) {
        return popGoodsService.batchQueryInfo(goodsIds);
    }

    @Override
    public List<GoodsInfoDTO> batchQueryPopGoods(List<Long> goodsIds) {
        return popGoodsService.batchQueryPopGoods(goodsIds);
    }

    @Override
    public List<QueryStatusCountBO> queryPopStatusCountListByCondition(QueryGoodsPageListRequest request) {
        return popGoodsService.queryPopStatusCountListByCondition(request);
    }

    @Override
    public PopGoodsDTO getPopGoodsByGoodsId(Long goodsId) {
        PopGoodsDTO popGoodsDTO = popGoodsService.getPopGoodsByGoodsId(goodsId);
        if (popGoodsDTO == null) {
            throw new BusinessException(GoodsErrorCode.GOODS_NOT_LINE);
        }
        return popGoodsDTO;
    }

    @Override
    public List<PopGoodsDTO> getPopGoodsListByGoodsIds(List<Long> goodsIds) {
        return popGoodsService.getPopGoodsListByGoodsIds(goodsIds);
    }

    @Override
    public EnterpriseGoodsCountBO getGoodsCountByEid(Long eid) {
        return popGoodsService.getGoodsCountByEid(eid);
    }

    @Override
    public List<GoodsListItemBO> findGoodsBySpecificationIdAndEids(Long specificationId, List<Long> eids) {
        return popGoodsService.findGoodsBySpecificationIdAndEids(specificationId,eids);
    }

    @Override
    public List<GoodsListItemBO> findGoodsBySpecificationIdListAndEidList(List<Long> specificationIdList, List<Long> eidList, GoodsStatusEnum goodsStatusEnum) {
        return popGoodsService.findGoodsBySpecificationIdListAndEidList(specificationIdList,eidList,goodsStatusEnum);
    }

    @Override
    public List<PopGoodsDTO> queryPopGoods(QueryGoodsPageListRequest request) {
        return popGoodsService.queryPopGoods(request);
    }

    @Override
    public Page<PopGoodsDTO> queryPopGoodsPage(QueryGoodsPageListRequest request) {
        return popGoodsService.queryPopGoodsPage(request);
    }
}
