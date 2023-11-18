package com.yiling.hmc.goods.api.impl;

import java.util.List;

import com.yiling.hmc.enterprise.dto.request.SyncGoodsSaveRequest;
import com.yiling.hmc.goods.dto.SyncGoodsDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.goods.api.GoodsApi;
import com.yiling.hmc.goods.bo.EnterpriseGoodsCountBO;
import com.yiling.hmc.goods.bo.HmcGoodsBO;
import com.yiling.hmc.goods.dto.HmcGoodsDTO;
import com.yiling.hmc.enterprise.dto.request.GoodsSaveListRequest;
import com.yiling.hmc.goods.dto.request.HmcGoodsPageRequest;
import com.yiling.hmc.goods.dto.request.QueryHmcGoodsRequest;
import com.yiling.hmc.goods.entity.HmcGoodsDO;
import com.yiling.hmc.goods.service.GoodsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsApiImpl implements GoodsApi {

    private final GoodsService goodsService;

    @Override
    public HmcGoodsDTO findById(Long id) {
        return goodsService.findById(id);
    }

    @Override
    public boolean saveGoodsList(GoodsSaveListRequest request) {
        return goodsService.saveGoodsList(request);
    }

    @Override
    public List<HmcGoodsDTO> listByEid(Long eId) {
        List<HmcGoodsDO> hmcGoodsDOList = goodsService.listByEid(eId);
        return PojoUtils.map(hmcGoodsDOList, HmcGoodsDTO.class);
    }

    @Override
    public List<HmcGoodsBO> batchQueryGoodsInfo(List<Long> ids) {
        return goodsService.batchQueryGoodsInfo(ids);
    }

    @Override
    public List<EnterpriseGoodsCountBO> countGoodsByEids(List<Long> eidList) {
        return goodsService.countGoodsByEids(eidList);
    }

    @Override
    public Page<HmcGoodsBO> pageListByEid(HmcGoodsPageRequest request) {
        return goodsService.pageListByEid(request);
    }

    @Override
    public List<HmcGoodsDTO> findBySpecificationsId(QueryHmcGoodsRequest request) {
        return goodsService.findBySpecificationsId(request);
    }

    @Override
    public HmcGoodsDTO findBySpecificationsIdAndEid(QueryHmcGoodsRequest request) {
        return goodsService.findBySpecificationsIdAndEid(request);
    }

    @Override
    public List<SyncGoodsDTO> syncGoodsToHmc(SyncGoodsSaveRequest request) {
        return goodsService.syncGoodsToHmc(request);
    }
}
