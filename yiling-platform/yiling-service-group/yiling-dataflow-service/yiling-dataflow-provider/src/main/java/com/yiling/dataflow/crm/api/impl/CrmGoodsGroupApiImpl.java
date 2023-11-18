package com.yiling.dataflow.crm.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmGoodsGroupApi;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupRelationDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsGroupPageRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsGroupRequest;
import com.yiling.dataflow.crm.service.CrmGoodsGroupRelationService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/8/8
 */
@DubboService
@Slf4j
public class CrmGoodsGroupApiImpl implements CrmGoodsGroupApi {

    @Autowired
    private CrmGoodsGroupService crmGoodsGroupService;

    @Autowired
    private CrmGoodsGroupRelationService crmGoodsGroupRelationService;

//    @Override
//    public List<CrmGoodsGroupDTO> getCrmGoodsGroupAll() {
//        return crmGoodsGroupService.getCrmGoodsGroupAll();
//    }

    @Override
    public List<CrmGoodsGroupDTO> findGroupByIds(List<Long> groupIds) {
        if(CollectionUtil.isEmpty(groupIds)){
            return ListUtil.empty();
        }
        return PojoUtils.map(crmGoodsGroupService.listByIds(groupIds),CrmGoodsGroupDTO.class);
    }

    @Override
    public CrmGoodsGroupDTO findGroupById(Long groupId) {
        return PojoUtils.map(crmGoodsGroupService.getById(groupId),CrmGoodsGroupDTO.class);
    }

    @Override
    public Long saveGroup(SaveOrUpdateCrmGoodsGroupRequest request) {
        return crmGoodsGroupService.saveGroup(request);
    }

    @Override
    public Long editGroup(SaveOrUpdateCrmGoodsGroupRequest request) {
        return crmGoodsGroupService.editGroup(request);
    }

    @Override
    public Page<CrmGoodsGroupDTO> queryGroupPage(QueryCrmGoodsGroupPageRequest request) {
        return crmGoodsGroupService.queryGroupPage(request);
    }

    @Override
    public List<Long> findGroupByGoodsCode(Long goodsCode) {
        return crmGoodsGroupRelationService.findGroupByGoodsCode(goodsCode);
    }

    @Override
    public Map<Long, List<Long>> findGroupByGoodsCodeList(List<Long> goodsCodeList) {
        return crmGoodsGroupRelationService.findGroupByGoodsCodeList(goodsCodeList);
    }

    @Override
    public List<CrmGoodsGroupRelationDTO> findGoodsRelationByGroupId(Long groupId) {
        return crmGoodsGroupRelationService.findGoodsRelationByGroupId(groupId);
    }

    @Override
    public Map<Long, List<CrmGoodsGroupRelationDTO>> findGoodsRelationByGroupIds(List<Long> groupIds) {
        return crmGoodsGroupRelationService.findGoodsRelationByGroupIds(groupIds);
    }

    @Override
    public Map<Long, List<Long>> findCrmDepartProductByGoodsCodeList(List<Long> goodsCodeList) {
        return crmGoodsGroupService.findCrmDepartProductByGoodsCodeList(goodsCodeList);
    }

    @Override
    public List<Long> findBakCrmDepartProductByGoodsCode(Long goodsCode, String tableSuffix) {
        return crmGoodsGroupService.findBakCrmDepartProductByGoodsCode(goodsCode,tableSuffix);
    }
}
