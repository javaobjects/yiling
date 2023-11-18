package com.yiling.goods.medicine.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.dao.RecommendGoodsGroupMapper;
import com.yiling.goods.medicine.dao.RecommendGoodsGroupRelationMapper;
import com.yiling.goods.medicine.dto.RecommendGoodsGroupDTO;
import com.yiling.goods.medicine.dto.RecommendGoodsGroupRelationDTO;
import com.yiling.goods.medicine.dto.request.QueryRecommendGoodsGroupPageRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateRecommendGoodsGroupRequest;
import com.yiling.goods.medicine.dto.request.SaveRecommendGoodsGroupRelationRequest;
import com.yiling.goods.medicine.entity.RecommendGoodsGroupDO;
import com.yiling.goods.medicine.entity.RecommendGoodsGroupRelationDO;
import com.yiling.goods.medicine.enums.GoodsErrorCode;
import com.yiling.goods.medicine.service.RecommendGoodsGroupService;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * @author shichen
 * @类名 RecommendGoodsGroupServiceImpl
 * @描述
 * @创建时间 2023/1/6
 * @修改人 shichen
 * @修改时间 2023/1/6
 **/
@Service
public class RecommendGoodsGroupServiceImpl extends BaseServiceImpl<RecommendGoodsGroupMapper, RecommendGoodsGroupDO> implements RecommendGoodsGroupService {

    @Autowired
    private RecommendGoodsGroupRelationMapper recommendGoodsGroupRelationMapper;

    @Override
    public Page<RecommendGoodsGroupDTO> queryGroupPage(QueryRecommendGoodsGroupPageRequest request) {
        Page<RecommendGoodsGroupDTO> groupPage = this.baseMapper.queryGroupPage(request.getPage(), request);
        if(CollectionUtil.isNotEmpty(groupPage.getRecords())){
            List<Long> groupIds = groupPage.getRecords().stream().map(RecommendGoodsGroupDTO::getId).collect(Collectors.toList());
            List<RecommendGoodsGroupRelationDTO> relationList = this.getGroupRelationByGroupIdsAndGoodsIds(groupIds,null);
            if(CollectionUtil.isNotEmpty(relationList)){
                Map<Long, List<RecommendGoodsGroupRelationDTO>> relationMap = relationList.stream().collect(Collectors.groupingBy(RecommendGoodsGroupRelationDTO::getGroupId));
                groupPage.getRecords().forEach(group->{
                    group.setRelationList(relationMap.get(group.getId()));
                });
            }
        }
        return groupPage;
    }

    @Override
    public RecommendGoodsGroupDTO getGroupById(Long id) {
        RecommendGoodsGroupDO groupDO = this.getById(id);
        if(null==groupDO){
            return null;
        }
        List<RecommendGoodsGroupRelationDTO> relationList = this.getGroupRelationByGroupIdsAndGoodsIds(ListUtil.toList(id), null);
        RecommendGoodsGroupDTO groupDTO = PojoUtils.map(groupDO, RecommendGoodsGroupDTO.class);
        groupDTO.setRelationList(relationList);
        return groupDTO;
    }

    @Override
    public List<RecommendGoodsGroupDTO> queryGroupList(QueryRecommendGoodsGroupPageRequest request) {
        LambdaQueryWrapper<RecommendGoodsGroupDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RecommendGoodsGroupDO::getEid,request.getEid());
        queryWrapper.eq(RecommendGoodsGroupDO::getQuickPurchaseFlag,request.getQuickPurchaseFlag());
        queryWrapper.orderByAsc(RecommendGoodsGroupDO::getId);
        if(null!=request.getQueryLimit() && request.getQueryLimit()>0){
            queryWrapper.last(" limit "+request.getQueryLimit());
        }
        List<RecommendGoodsGroupDO> doList = this.list(queryWrapper);
        List<RecommendGoodsGroupDTO> dtoList = PojoUtils.map(doList, RecommendGoodsGroupDTO.class);
        if(CollectionUtil.isNotEmpty(dtoList)){
            List<Long> groupIds = dtoList.stream().map(RecommendGoodsGroupDTO::getId).collect(Collectors.toList());
            List<RecommendGoodsGroupRelationDTO> relationList = this.getGroupRelationByGroupIdsAndGoodsIds(groupIds,null);
            if(CollectionUtil.isNotEmpty(relationList)){
                Map<Long, List<RecommendGoodsGroupRelationDTO>> relationMap = relationList.stream().collect(Collectors.groupingBy(RecommendGoodsGroupRelationDTO::getGroupId));
                dtoList.forEach(group->{
                    group.setRelationList(relationMap.get(group.getId()));
                });
            }
        }
        return dtoList;
    }

    @Override
    public List<RecommendGoodsGroupRelationDTO> getGroupRelationByGroupIdsAndGoodsIds(List<Long> groupIds,List<Long> goodsIds) {
        if(CollectionUtil.isEmpty(groupIds)){
            return ListUtil.empty();
        }
        LambdaQueryWrapper<RecommendGoodsGroupRelationDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(RecommendGoodsGroupRelationDO::getGroupId,groupIds);
        if(CollectionUtil.isNotEmpty(goodsIds)){
            queryWrapper.in(RecommendGoodsGroupRelationDO::getGoodsId,goodsIds);
        }
        List<RecommendGoodsGroupRelationDO> relationDOS = recommendGoodsGroupRelationMapper.selectList(queryWrapper);
        return PojoUtils.map(relationDOS,RecommendGoodsGroupRelationDTO.class);
    }

    @Override
    public Long saveOrUpdateGroup(SaveOrUpdateRecommendGoodsGroupRequest request) {
        LambdaQueryWrapper<RecommendGoodsGroupDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RecommendGoodsGroupDO::getEid,request.getEid());
        queryWrapper.eq(RecommendGoodsGroupDO::getName,request.getName());
        queryWrapper.last(" limit 1");
        RecommendGoodsGroupDO goodsGroupDO = this.getOne(queryWrapper);
        if(null!=goodsGroupDO && !goodsGroupDO.getId().equals(request.getId())){
            throw new BusinessException(GoodsErrorCode.REPETITION,"商品组名称已存在");
        }
        RecommendGoodsGroupDO groupDO = PojoUtils.map(request, RecommendGoodsGroupDO.class);
        this.saveOrUpdate(groupDO);
        List<RecommendGoodsGroupRelationDTO> relationDTOList = this.getGroupRelationByGroupIdsAndGoodsIds(ListUtil.toList(groupDO.getId()), null);
        List<Long> goodsIds = relationDTOList.stream().map(RecommendGoodsGroupRelationDTO::getGoodsId).collect(Collectors.toList());
        List<Long> saveGoodsIds = ListUtil.toList();
        List<Long> deleteGoodsIds = ListUtil.toList();
        if(CollectionUtil.isNotEmpty(request.getRelationList())){
            request.getRelationList().forEach(relation->{
                if(!goodsIds.contains(relation.getGoodsId())){
                    saveGoodsIds.add(relation.getGoodsId());
                }
            });
            List<Long> requestGoodsIds = request.getRelationList().stream().map(SaveRecommendGoodsGroupRelationRequest::getGoodsId).collect(Collectors.toList());
            goodsIds.forEach(goodsId->{
                if(!requestGoodsIds.contains(goodsId)){
                    deleteGoodsIds.add(goodsId);
                }
            });
        }else {
            deleteGoodsIds.addAll(goodsIds);
        }
        if(CollectionUtil.isNotEmpty(saveGoodsIds)){
            this.batchSaveGroupRelation(groupDO.getId(),saveGoodsIds,request.getOpUserId());
        }
        if(CollectionUtil.isNotEmpty(deleteGoodsIds)){
            this.batchDeleteGroupRelation(groupDO.getId(),deleteGoodsIds,request.getOpUserId());
        }
        return groupDO.getId();
    }

    @Override
    public int deleteGroup(Long groupId, Long opUserId) {
        RecommendGoodsGroupDO groupDO = new RecommendGoodsGroupDO();
        groupDO.setId(groupId);
        groupDO.setOpUserId(opUserId);
        return this.deleteByIdWithFill(groupDO);
    }

    @Override
    public int batchSaveGroupRelation(Long groupId, List<Long> goodsIds,Long opUserId) {
        if(CollectionUtil.isEmpty(goodsIds)){
            return 0;
        }
        return recommendGoodsGroupRelationMapper.batchSaveGroupRelation(groupId,goodsIds,opUserId);
    }

    @Override
    public int batchDeleteGroupRelation(Long groupId, List<Long> goodsIds,Long opUserId) {
        if(CollectionUtil.isEmpty(goodsIds)){
            return 0;
        }
        LambdaQueryWrapper<RecommendGoodsGroupRelationDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RecommendGoodsGroupRelationDO::getGroupId,groupId);
        queryWrapper.in(RecommendGoodsGroupRelationDO::getGoodsId,goodsIds);
        RecommendGoodsGroupRelationDO relationDO=new RecommendGoodsGroupRelationDO();
        relationDO.setOpUserId(opUserId);
        relationDO.setOpTime(new Date());
        return recommendGoodsGroupRelationMapper.batchDeleteWithFill(relationDO,queryWrapper);
    }
}
