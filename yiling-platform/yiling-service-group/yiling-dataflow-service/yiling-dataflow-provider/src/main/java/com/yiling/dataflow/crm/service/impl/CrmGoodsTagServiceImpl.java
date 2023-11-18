package com.yiling.dataflow.crm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.bo.CrmGoodsTagRelationBO;
import com.yiling.dataflow.crm.dao.CrmGoodsTagMapper;
import com.yiling.dataflow.crm.dto.CrmGoodsTagDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsTagRelationDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsTagPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsTagRelationPageRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsTagRequest;
import com.yiling.dataflow.crm.entity.CrmGoodsTagDO;
import com.yiling.dataflow.crm.entity.CrmGoodsTagRelationDO;
import com.yiling.dataflow.crm.enums.CrmGoodsErrorCode;
import com.yiling.dataflow.crm.service.CrmGoodsTagRelationService;
import com.yiling.dataflow.crm.service.CrmGoodsTagService;
import com.yiling.framework.common.annotations.DynamicName;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 CrmGoodsTagServiceImpl
 * @描述
 * @创建时间 2023/4/7
 * @修改人 shichen
 * @修改时间 2023/4/7
 **/
@Slf4j
@Service
public class CrmGoodsTagServiceImpl extends BaseServiceImpl<CrmGoodsTagMapper, CrmGoodsTagDO> implements CrmGoodsTagService {

    @Autowired
    private CrmGoodsTagRelationService crmGoodsTagRelationService;

    @Override
    public Long saveOrUpdateTag(SaveOrUpdateCrmGoodsTagRequest request) {
        log.info("保存商品标签参数,request:{}",request);
        CrmGoodsTagDTO tagDTO = this.findTagByName(request.getName());
        if(null== request.getId() || request.getId()==0){
            if(null!=tagDTO){
                throw new BusinessException(CrmGoodsErrorCode.REPEAT_ERROR,"标签名已存在");
            }
            CrmGoodsTagDO tagDO = PojoUtils.map(request, CrmGoodsTagDO.class);
            this.save(tagDO);
            return tagDO.getId();
        }else {
            //标签类型无法编辑
            request.setType(null);
            if(null!=tagDTO && !tagDTO.getId().equals(request.getId())){
                throw new BusinessException(CrmGoodsErrorCode.REPEAT_ERROR,"标签名已存在");
            }
            CrmGoodsTagDO tagDO = PojoUtils.map(request, CrmGoodsTagDO.class);
            this.updateById(tagDO);
            return request.getId();
        }
    }

    @Override
    public void deleteTag(Long id, Long opUserId) {
        CrmGoodsTagDO deleteDO = new CrmGoodsTagDO();
        deleteDO.setId(id);
        deleteDO.setOpUserId(opUserId);
        this.deleteByIdWithFill(deleteDO);
        CrmGoodsTagRelationDO deleteRelationDO = new CrmGoodsTagRelationDO();
        deleteRelationDO.setOpUserId(opUserId);
        QueryWrapper<CrmGoodsTagRelationDO> relationWrapper = new QueryWrapper();
        relationWrapper.lambda().eq(CrmGoodsTagRelationDO::getTagId,id);
        crmGoodsTagRelationService.batchDeleteWithFill(deleteRelationDO,relationWrapper);
    }

    @Override
    public Page<CrmGoodsTagDTO> queryTagPage(QueryCrmGoodsTagPageRequest request) {
        QueryWrapper<CrmGoodsTagDO> queryWrapper = new QueryWrapper();
        if(StringUtils.isNotBlank(request.getName())){
            queryWrapper.lambda().like(CrmGoodsTagDO::getName,request.getName());
        }
        if(null!=request.getType() && request.getType()!=0){
            queryWrapper.lambda().eq(CrmGoodsTagDO::getType,request.getType());
        }
        queryWrapper.lambda().orderByDesc(CrmGoodsTagDO::getId);
        Page<CrmGoodsTagDO> page = this.page(request.getPage(), queryWrapper);
        return PojoUtils.map(page,CrmGoodsTagDTO.class);
    }

    @Override
    public List<CrmGoodsTagDTO> getTagList(Integer type) {
        QueryWrapper<CrmGoodsTagDO> queryWrapper = new QueryWrapper();
        if(null!=type && type!=0){
            queryWrapper.lambda().eq(CrmGoodsTagDO::getType,type);
        }
        return PojoUtils.map(this.list(queryWrapper),CrmGoodsTagDTO.class);
    }

    @Override
    public Long saveTagRelation(Long tagId, Long crmGoodsId, Long opUserId) {
        CrmGoodsTagRelationDTO relation = this.crmGoodsTagRelationService.findRelationByTagIdAndGoodsId(tagId, crmGoodsId);
        if(null!=relation){
            return relation.getId();
        }
        CrmGoodsTagRelationDO saveDO = new CrmGoodsTagRelationDO();
        saveDO.setTagId(tagId);
        saveDO.setCrmGoodsId(crmGoodsId);
        saveDO.setOpUserId(opUserId);
        crmGoodsTagRelationService.save(saveDO);
        return saveDO.getId();
    }

    @Override
    public Boolean batchSaveTagsByGoods(List<Long> tagIds, Long crmGoodsId, Long opUserId) {
        log.info("根据商品保存商品标签参数,tagIds:{},crmGoodsId:{}",tagIds,crmGoodsId);
        List<CrmGoodsTagDTO> dataTags = this.findTagByGoodsId(crmGoodsId);
        List<Long> dataTagIds = dataTags.stream().map(CrmGoodsTagDTO::getId).distinct().collect(Collectors.toList());
        if(CollectionUtil.isEmpty(tagIds)){
            if(CollectionUtil.isNotEmpty(dataTagIds)){
                this.batchDeleteTagRelation(dataTagIds,crmGoodsId,opUserId);
            }
            return true;
        }
        if(CollectionUtil.isEmpty(dataTagIds)){
            this.batchSaveTagRelation(tagIds,crmGoodsId,opUserId);
            return true;
        }
        List<Long> deleteTagList = ListUtil.toList();
        List<Long> saveTagList = ListUtil.toList();
        dataTagIds.forEach(tagId->{
            if(!tagIds.contains(tagId)){
                deleteTagList.add(tagId);
            }
        });
        tagIds.forEach(tagId->{
            if(!dataTagIds.contains(tagId)){
                saveTagList.add(tagId);
            }
        });
        if(CollectionUtil.isNotEmpty(deleteTagList)){
            this.batchDeleteTagRelation(deleteTagList,crmGoodsId,opUserId);
        }
        if(CollectionUtil.isNotEmpty(saveTagList)){
            this.batchSaveTagRelation(saveTagList,crmGoodsId,opUserId);
        }
        return true;
    }

    @Override
    public void deleteTagRelation(Long id, Long opUserId) {
        CrmGoodsTagRelationDO deleteDO = new CrmGoodsTagRelationDO();
        deleteDO.setId(id);
        deleteDO.setOpUserId(opUserId);
        crmGoodsTagRelationService.deleteByIdWithFill(deleteDO);
    }

    @Override
    public Page<CrmGoodsTagRelationBO> queryTagRelationPage(QueryCrmGoodsTagRelationPageRequest request) {
        return this.crmGoodsTagRelationService.queryTagRelationPage(request);
    }

    @Override
    public List<CrmGoodsTagDTO> findTagByGoodsId(Long crmGoodsId) {
        return PojoUtils.map(this.baseMapper.findTagByGoodsId(crmGoodsId),CrmGoodsTagDTO.class);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<CrmGoodsTagDTO> findBakTagByGoodsId(Long crmGoodsId, String tableSuffix) {
        List<CrmGoodsTagRelationDTO> relationList = crmGoodsTagRelationService.getBakRelationByGoodsId(crmGoodsId,tableSuffix);
        if(CollectionUtil.isEmpty(relationList)){
            return ListUtil.empty();
        }
        List<Long> tagIds = relationList.stream().map(CrmGoodsTagRelationDTO::getTagId).distinct().collect(Collectors.toList());
        return PojoUtils.map(this.listByIds(tagIds),CrmGoodsTagDTO.class);
    }

    @Override
    public Map<Long, List<CrmGoodsTagRelationBO>> findTagByGoodsIds(List<Long> crmGoodsIds) {
        List<CrmGoodsTagRelationBO> relationBOList = this.crmGoodsTagRelationService.findRelationByGoodsIds(crmGoodsIds);
        if(CollectionUtil.isEmpty(relationBOList)){
            return MapUtil.empty();
        }
        Map<Long, List<CrmGoodsTagRelationBO>> map = relationBOList.stream().collect(Collectors.groupingBy(CrmGoodsTagRelationBO::getCrmGoodsId));
        return map;
    }

    @Override
    public Map<Long, Long> countTagGoods(List<Long> tagIds) {
        List<Map<Long, Long>> listMap = this.crmGoodsTagRelationService.countTagGoods(tagIds);
        if(CollectionUtil.isEmpty(listMap)){
            return MapUtil.empty();
        }
        Map<Long, Long> map = new HashMap<>(listMap.size());
        listMap.forEach(
                item -> {
                    map.put(item.get("tagId"), item.get("goodsCount"));
                }
        );
        return map;
    }

    private CrmGoodsTagDTO findTagByName(String name){
        QueryWrapper<CrmGoodsTagDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(CrmGoodsTagDO::getName,name);
        queryWrapper.lambda().last("limit 1");
        return PojoUtils.map(this.getOne(queryWrapper),CrmGoodsTagDTO.class);
    }

    private Boolean batchSaveTagRelation(List<Long> tagIds, Long crmGoodsId, Long opUserId){
        List<CrmGoodsTagRelationDO> relationList=tagIds.stream().map(tagId->{
            CrmGoodsTagRelationDO relationDO = new CrmGoodsTagRelationDO();
            relationDO.setTagId(tagId);
            relationDO.setCrmGoodsId(crmGoodsId);
            relationDO.setOpUserId(opUserId);
            return relationDO;
        }).collect(Collectors.toList());
        this.crmGoodsTagRelationService.saveBatch(relationList);
        return true;
    }

    private Boolean batchDeleteTagRelation(List<Long> tagIds, Long crmGoodsId, Long opUserId){
        QueryWrapper<CrmGoodsTagRelationDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(CrmGoodsTagRelationDO::getCrmGoodsId,crmGoodsId);
        queryWrapper.lambda().in(CrmGoodsTagRelationDO::getTagId,tagIds);
        CrmGoodsTagRelationDO deleteDO = new CrmGoodsTagRelationDO();
        deleteDO.setOpUserId(opUserId);
        this.crmGoodsTagRelationService.batchDeleteWithFill(deleteDO,queryWrapper);
        return true;
    }
}
