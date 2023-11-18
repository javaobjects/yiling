package com.yiling.goods.medicine.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.goods.medicine.api.RecommendGoodsGroupApi;
import com.yiling.goods.medicine.dto.RecommendGoodsGroupDTO;
import com.yiling.goods.medicine.dto.RecommendGoodsGroupRelationDTO;
import com.yiling.goods.medicine.dto.request.QueryRecommendGoodsGroupPageRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateRecommendGoodsGroupRequest;
import com.yiling.goods.medicine.service.RecommendGoodsGroupService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 RecommendGoodsGroupApiImpl
 * @描述
 * @创建时间 2023/1/9
 * @修改人 shichen
 * @修改时间 2023/1/9
 **/
@DubboService
@Slf4j
public class RecommendGoodsGroupApiImpl implements RecommendGoodsGroupApi {
    @Autowired
    private RecommendGoodsGroupService recommendGoodsGroupService;

    @Override
    public Page<RecommendGoodsGroupDTO> queryGroupPage(QueryRecommendGoodsGroupPageRequest request) {
        return recommendGoodsGroupService.queryGroupPage(request);
    }

    @Override
    public RecommendGoodsGroupDTO getGroupById(Long id) {
        return recommendGoodsGroupService.getGroupById(id);
    }

    @Override
    public List<RecommendGoodsGroupDTO> queryGroupList(QueryRecommendGoodsGroupPageRequest request) {
        return recommendGoodsGroupService.queryGroupList(request);
    }

    @Override
    public List<RecommendGoodsGroupRelationDTO> getGroupRelationByGroupIdsAndGoodsIds(List<Long> groupIds, List<Long> goodsIds) {
        return recommendGoodsGroupService.getGroupRelationByGroupIdsAndGoodsIds(groupIds,goodsIds);
    }

    @Override
    public Long saveOrUpdateGroup(SaveOrUpdateRecommendGoodsGroupRequest request) {
        return recommendGoodsGroupService.saveOrUpdateGroup(request);
    }

    @Override
    public int deleteGroup(Long groupId, Long opUserId) {
        return recommendGoodsGroupService.deleteGroup(groupId,opUserId);
    }

    @Override
    public int batchSaveGroupRelation(Long groupId, List<Long> goodsIds, Long opUserId) {
        return recommendGoodsGroupService.batchSaveGroupRelation(groupId,goodsIds,opUserId);
    }

    @Override
    public int batchDeleteGroupRelation(Long groupId, List<Long> goodsIds, Long opUserId) {
        return recommendGoodsGroupService.batchDeleteGroupRelation(groupId,goodsIds,opUserId);
    }
}
