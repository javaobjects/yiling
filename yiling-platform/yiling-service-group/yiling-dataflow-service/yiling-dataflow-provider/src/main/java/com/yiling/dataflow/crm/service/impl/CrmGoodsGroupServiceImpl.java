package com.yiling.dataflow.crm.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.CrmDepartmentProductRelationDTO;
import com.yiling.dataflow.agency.service.CrmDepartmentProductRelationService;
import com.yiling.dataflow.crm.dao.CrmGoodsGroupMapper;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsGroupPageRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsGroupRequest;
import com.yiling.dataflow.crm.entity.CrmGoodsGroupDO;
import com.yiling.dataflow.crm.enums.CrmGoodsErrorCode;
import com.yiling.dataflow.crm.service.CrmGoodsGroupRelationService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupService;
import com.yiling.framework.common.annotations.DynamicName;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-09-16
 */
@Slf4j
@Service
public class CrmGoodsGroupServiceImpl extends BaseServiceImpl<CrmGoodsGroupMapper, CrmGoodsGroupDO> implements CrmGoodsGroupService {

    @Autowired
    private CrmGoodsGroupRelationService crmGoodsGroupRelationService;

    @Autowired
    private CrmDepartmentProductRelationService crmDepartmentProductRelationService;

    @Override
    public Long saveGroup(SaveOrUpdateCrmGoodsGroupRequest request) {
        log.info("保存商品组参数,request:{}",request);
        CrmGoodsGroupDO group = this.findGroupByCodeOrName(null, request.getName());
        if(null != group){
            throw new BusinessException(CrmGoodsErrorCode.REPEAT_ERROR,"产品组名称已存在");
        }
        CrmGoodsGroupDO saveDO = PojoUtils.map(request, CrmGoodsGroupDO.class);
        this.save(saveDO);
        this.crmGoodsGroupRelationService.batchSaveGroupRelation(request.getGoodsRelationList(),saveDO.getId(),request.getOpUserId());
        this.crmDepartmentProductRelationService.batchSaveRelationByGroup(request.getDepartmentRelationList(),saveDO.getId(),saveDO.getName(),request.getOpUserId());
        String groupCode = "PG"+String.format("%04d", saveDO.getId());
        CrmGoodsGroupDO updateDO = new CrmGoodsGroupDO();
        updateDO.setId(saveDO.getId());
        updateDO.setCode(groupCode);
        this.updateById(updateDO);
        return saveDO.getId();
    }

    @Override
    public Long editGroup(SaveOrUpdateCrmGoodsGroupRequest request) {
        log.info("编辑商品组参数,request:{}",request);
        CrmGoodsGroupDO group = this.findGroupByCodeOrName(null, request.getName());
        if(null != group && !group.getId().equals(request.getId())){
            throw new BusinessException(CrmGoodsErrorCode.REPEAT_ERROR,"产品组名称已存在");
        }
        CrmGoodsGroupDO editDO = PojoUtils.map(request, CrmGoodsGroupDO.class);
        this.updateById(editDO);
        this.crmGoodsGroupRelationService.batchSaveGroupRelation(request.getGoodsRelationList(),request.getId(),request.getOpUserId());
        this.crmDepartmentProductRelationService.batchSaveRelationByGroup(request.getDepartmentRelationList(),request.getId(),editDO.getName(),request.getOpUserId());
        return request.getId();
    }

    @Override
    public Page<CrmGoodsGroupDTO> queryGroupPage(QueryCrmGoodsGroupPageRequest request) {
        Page<CrmGoodsGroupDO> page = this.baseMapper.queryGroupPage(request.getPage(), request);
        return PojoUtils.map(page,CrmGoodsGroupDTO.class);
    }

    @Override
    public List<CrmGoodsGroupDTO> findByIdsAndStatus(List<Long> groupIds, Integer status) {
        if(CollectionUtil.isEmpty(groupIds)){
            return ListUtil.empty();
        }
        QueryWrapper<CrmGoodsGroupDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(CrmGoodsGroupDO::getId,groupIds);
        if(null != status){
            queryWrapper.lambda().eq(CrmGoodsGroupDO::getStatus,status);
        }
        List<CrmGoodsGroupDO> list = this.list(queryWrapper);
        return PojoUtils.map(list,CrmGoodsGroupDTO.class);
    }

    private CrmGoodsGroupDO findGroupByCodeOrName(String code,String name){
        if(StringUtils.isNotBlank(code) && StringUtils.isNotBlank(name)){
            throw new BusinessException(CrmGoodsErrorCode.EMPTY_ERROR,"产品组编码和名称都为空");
        }
        QueryWrapper<CrmGoodsGroupDO> queryWrapper = new QueryWrapper();
        if(StringUtils.isNotBlank(code)){
            queryWrapper.lambda().eq(CrmGoodsGroupDO::getCode,code);
        }
        if(StringUtils.isNotBlank(name)){
            queryWrapper.lambda().eq(CrmGoodsGroupDO::getName,name);
        }
        queryWrapper.lambda().last("limit 1");
        return this.getOne(queryWrapper);
    }

    @Override
    public List<CrmGoodsGroupDTO> findGroupByIds(List<Long> groupIds) {
        if(CollectionUtil.isEmpty(groupIds)){
            return ListUtil.empty();
        }
        return PojoUtils.map(this.listByIds(groupIds),CrmGoodsGroupDTO.class);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<CrmGoodsGroupDTO> findBakGroupByIds(List<Long> groupIds,String tableSuffix) {
        if(CollectionUtil.isEmpty(groupIds)){
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CrmGoodsGroupDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CrmGoodsGroupDO::getId,groupIds);
        return PojoUtils.map(this.list(queryWrapper),CrmGoodsGroupDTO.class);
    }

    @Override
    public List<Long> findCrmDepartProductGroupByGoodsCode(Long goodsCode) {
        List<Long> groupIds = crmGoodsGroupRelationService.findGroupByGoodsCode(goodsCode);
        List<CrmDepartmentProductRelationDTO> departmentRelationList = crmDepartmentProductRelationService.getByGroupIds(groupIds);
        return departmentRelationList.stream().map(CrmDepartmentProductRelationDTO::getId).collect(Collectors.toList());
    }

    @Override
    public List<Long> findBakCrmDepartProductByGoodsCode(Long goodsCode, String tableSuffix) {
        List<Long> groupIds = crmGoodsGroupRelationService.findBakGroupByGoodsCode(goodsCode,tableSuffix);
        List<CrmDepartmentProductRelationDTO> departmentRelationList = crmDepartmentProductRelationService.getBakByGroupIds(groupIds,tableSuffix);
        return departmentRelationList.stream().map(CrmDepartmentProductRelationDTO::getId).collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<Long>> findCrmDepartProductByGoodsCodeList(List<Long> goodsCodeList) {
        HashMap<Long, List<Long>> map = MapUtil.newHashMap();
        Map<Long, List<Long>> goodsGroupMap = crmGoodsGroupRelationService.findGroupByGoodsCodeList(goodsCodeList);
        if(CollectionUtil.isEmpty(goodsGroupMap)){
            return map;
        }
        List<Long> groupIds= goodsGroupMap.values().stream().flatMap(Collection::stream).distinct().collect(Collectors.toList());
        List<CrmDepartmentProductRelationDTO> departmentRelationList = crmDepartmentProductRelationService.getByGroupIds(groupIds);
        Map<Long, List<Long>> groupDepartMap = departmentRelationList.stream().collect(Collectors.groupingBy(CrmDepartmentProductRelationDTO::getProductGroupId, Collectors.mapping(CrmDepartmentProductRelationDTO::getId, Collectors.toList())));
        goodsGroupMap.entrySet().forEach(ggEntry->{
            List<Long> departRelationIds = ListUtil.toList();
            ggEntry.getValue().forEach(groupId->{
                List<Long> cdprIds = groupDepartMap.get(groupId);
                if(CollectionUtil.isNotEmpty(cdprIds)){
                    departRelationIds.addAll(cdprIds);
                }
            });
            if(CollectionUtil.isNotEmpty(departRelationIds)){
                map.put(ggEntry.getKey(),departRelationIds.stream().distinct().collect(Collectors.toList()));
            }
        });
        return map;
    }
}
