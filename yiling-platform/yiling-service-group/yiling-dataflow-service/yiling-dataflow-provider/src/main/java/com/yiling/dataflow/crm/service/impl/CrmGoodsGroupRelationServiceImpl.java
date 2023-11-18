package com.yiling.dataflow.crm.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.dataflow.crm.dao.CrmGoodsGroupRelationMapper;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupRelationDTO;
import com.yiling.dataflow.crm.dto.request.SaveCrmGoodsGroupRelationRequest;
import com.yiling.dataflow.crm.entity.CrmGoodsGroupRelationDO;
import com.yiling.dataflow.crm.service.CrmGoodsGroupRelationService;
import com.yiling.framework.common.annotations.DynamicName;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 CrmGoodsGroupRelationServiceImpl
 * @描述
 * @创建时间 2023/3/7
 * @修改人 shichen
 * @修改时间 2023/3/7
 **/
@Slf4j
@Service
public class CrmGoodsGroupRelationServiceImpl extends BaseServiceImpl<CrmGoodsGroupRelationMapper, CrmGoodsGroupRelationDO> implements CrmGoodsGroupRelationService {
    @Override
    public List<Long> findGroupByGoodsCode(Long goodsCode) {
        QueryWrapper<CrmGoodsGroupRelationDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(CrmGoodsGroupRelationDO::getGoodsCode,goodsCode);
        List<CrmGoodsGroupRelationDO> list = this.list(queryWrapper);
        List<Long> idList = list.stream().map(CrmGoodsGroupRelationDO::getGroupId).collect(Collectors.toList());
        return idList;
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<Long> findBakGroupByGoodsCode(Long goodsCode, String tableSuffix) {
        QueryWrapper<CrmGoodsGroupRelationDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(CrmGoodsGroupRelationDO::getGoodsCode,goodsCode);
        List<CrmGoodsGroupRelationDO> list = this.list(queryWrapper);
        List<Long> idList = list.stream().map(CrmGoodsGroupRelationDO::getGroupId).collect(Collectors.toList());
        return idList;
    }

    @Override
    public Map<Long, List<Long>> findGroupByGoodsCodeList(List<Long> goodsCodeList) {
        if(CollectionUtil.isEmpty(goodsCodeList)){
            return MapUtil.empty();
        }
        QueryWrapper<CrmGoodsGroupRelationDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(CrmGoodsGroupRelationDO::getGoodsCode,goodsCodeList);
        List<CrmGoodsGroupRelationDO> list = this.list(queryWrapper);
        Map<Long, List<Long>> listMap = list.stream().collect(Collectors.groupingBy(CrmGoodsGroupRelationDO::getGoodsCode, Collectors.mapping(CrmGoodsGroupRelationDO::getGroupId, Collectors.toList())));
        return listMap;
    }

    @Override
    public List<CrmGoodsGroupRelationDTO> findGoodsRelationByGroupId(Long groupId) {
        QueryWrapper<CrmGoodsGroupRelationDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(CrmGoodsGroupRelationDO::getGroupId,groupId);
        List<CrmGoodsGroupRelationDO> list = this.list(queryWrapper);
        return PojoUtils.map(list,CrmGoodsGroupRelationDTO.class);
    }

    @Override
    public Map<Long,List<CrmGoodsGroupRelationDTO>> findGoodsRelationByGroupIds(List<Long> groupIds) {
        if(CollectionUtil.isEmpty(groupIds)){
            return MapUtil.empty();
        }
        QueryWrapper<CrmGoodsGroupRelationDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(CrmGoodsGroupRelationDO::getGroupId,groupIds);
        List<CrmGoodsGroupRelationDO> list = this.list(queryWrapper);
        if(CollectionUtil.isEmpty(list)){
            return MapUtil.empty();
        }
        List<CrmGoodsGroupRelationDTO> dtoList = PojoUtils.map(list, CrmGoodsGroupRelationDTO.class);
        return dtoList.stream().collect(Collectors.groupingBy(CrmGoodsGroupRelationDTO::getGroupId));
    }

    @Override
    public Boolean batchSaveGroupRelation(List<SaveCrmGoodsGroupRelationRequest> requestList,Long groupId,Long opUserId) {
        log.info("批量保存商品组关联参数,requestList:{},groupId:{}",requestList,groupId);
        //产品组
        if(CollectionUtil.isEmpty(requestList)){
            return true;
        }
        List<CrmGoodsGroupRelationDTO> groupRelationList = this.findGoodsRelationByGroupId(groupId);
        if(CollectionUtil.isNotEmpty(groupRelationList)){
            Map<Long, CrmGoodsGroupRelationDTO> dataMap = groupRelationList.stream().collect(Collectors.toMap(CrmGoodsGroupRelationDTO::getGoodsId, Function.identity(),(e1,e2)->e1));
            List<SaveCrmGoodsGroupRelationRequest> saveList = ListUtil.toList();
            List<SaveCrmGoodsGroupRelationRequest> updateList = ListUtil.toList();
            requestList.forEach(request->{
                CrmGoodsGroupRelationDTO dataRelation = dataMap.get(request.getGoodsId());
                if(null == dataRelation){
                    saveList.add(request);
                }else {
                    request.setId(dataRelation.getId());
                    updateList.add(request);
                }
            });
            if(CollectionUtil.isNotEmpty(updateList)){
                this.updateByRelations(updateList,opUserId);
            }
            if(CollectionUtil.isNotEmpty(saveList)){
                this.saveAll(saveList,groupId,opUserId);
            }
        }else {
            this.saveAll(requestList,groupId,opUserId);
        }
        return true;
    }

    private void deleteByRelationIds(List<Long> relationIds,Long opUserId){
        QueryWrapper<CrmGoodsGroupRelationDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(CrmGoodsGroupRelationDO::getId,relationIds);
        CrmGoodsGroupRelationDO deleteDO = new CrmGoodsGroupRelationDO();
        deleteDO.setOpUserId(opUserId);
        this.batchDeleteWithFill(deleteDO,queryWrapper);
    }

    private void saveAll(List<SaveCrmGoodsGroupRelationRequest> requestList,Long groupId,Long opUserId){
        List<CrmGoodsGroupRelationDO> saveList = requestList.stream().map(request->{
            CrmGoodsGroupRelationDO saveDO = new CrmGoodsGroupRelationDO();
            saveDO.setGoodsId(request.getGoodsId());
            saveDO.setGoodsCode(request.getGoodsCode());
            saveDO.setStatus(request.getStatus());
            saveDO.setGroupId(groupId);
            saveDO.setOpUserId(opUserId);
            return saveDO;
        }).collect(Collectors.toList());
        this.saveBatch(saveList);
    }

    private void updateByRelations(List<SaveCrmGoodsGroupRelationRequest> requestList,Long opUserId){
        List<CrmGoodsGroupRelationDO> updateList = PojoUtils.map(requestList, CrmGoodsGroupRelationDO.class);
        updateList.forEach(updateDO->{
            updateDO.setOpUserId(opUserId);
        });
        this.updateBatchById(updateList);
    }
}
