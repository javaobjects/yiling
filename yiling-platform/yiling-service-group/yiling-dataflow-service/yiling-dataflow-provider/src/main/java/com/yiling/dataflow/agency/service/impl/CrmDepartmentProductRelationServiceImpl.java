package com.yiling.dataflow.agency.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.dataflow.agency.dto.CrmDepartmentProductRelationDTO;
import com.yiling.dataflow.agency.dto.request.SaveOrUpdateCrmDepartProductGroupRequest;
import com.yiling.dataflow.agency.entity.CrmDepartmentProductRelationDO;
import com.yiling.dataflow.agency.dao.CrmDepartmentProductRelationMapper;
import com.yiling.dataflow.agency.service.CrmDepartmentProductRelationService;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.framework.common.annotations.DynamicName;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;

/**
 * <p>
 * 业务部门与产品组对应关系表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2023-02-16
 */
@Service
public class CrmDepartmentProductRelationServiceImpl extends BaseServiceImpl<CrmDepartmentProductRelationMapper, CrmDepartmentProductRelationDO> implements CrmDepartmentProductRelationService {

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<CrmDepartmentProductRelationDO> getByIds(List<Long> ids, String tableSuffix) {
        LambdaQueryWrapper<CrmDepartmentProductRelationDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CrmDepartmentProductRelationDO::getId,ids);
        return this.list(queryWrapper);
    }

    @Override
    public List<CrmDepartmentProductRelationDO> getByIdList(List<Long> ids) {
        LambdaQueryWrapper<CrmDepartmentProductRelationDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CrmDepartmentProductRelationDO::getId,ids);
        return this.list(queryWrapper);
    }

    @Override
    public List<CrmDepartmentProductRelationDTO> getByGroupId(Long groupId) {
        LambdaQueryWrapper<CrmDepartmentProductRelationDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CrmDepartmentProductRelationDO::getProductGroupId,groupId);
        return PojoUtils.map(this.list(queryWrapper),CrmDepartmentProductRelationDTO.class);
    }

    @Override
    public List<CrmDepartmentProductRelationDTO> getByGroupIds(List<Long> groupIds) {
        if(CollectionUtil.isEmpty(groupIds)){
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CrmDepartmentProductRelationDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CrmDepartmentProductRelationDO::getProductGroupId,groupIds);
        return PojoUtils.map(this.list(queryWrapper),CrmDepartmentProductRelationDTO.class);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<CrmDepartmentProductRelationDTO> getBakByGroupIds(List<Long> groupIds, String tableSuffix) {
        if(CollectionUtil.isEmpty(groupIds)){
            return ListUtil.empty();
        }
        LambdaQueryWrapper<CrmDepartmentProductRelationDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CrmDepartmentProductRelationDO::getProductGroupId,groupIds);
        return PojoUtils.map(this.list(queryWrapper),CrmDepartmentProductRelationDTO.class);
    }

    @Override
    public Boolean batchSaveRelationByGroup(List<SaveOrUpdateCrmDepartProductGroupRequest> requestList, Long groupId,String groupName, Long opUserId) {
        List<CrmDepartmentProductRelationDTO> dataRelationList = this.getByGroupId(groupId);
        List<Long> dataIdList = dataRelationList.stream().map(CrmDepartmentProductRelationDTO::getId).collect(Collectors.toList());
        if(CollectionUtil.isEmpty(requestList)){
            if(CollectionUtil.isNotEmpty(dataIdList)){
                this.batchDelete(dataIdList,opUserId);
            }
            return true;
        }
        if(CollectionUtil.isEmpty(dataIdList)){
            this.batchSave(requestList,groupId,groupName,opUserId);
            return true;
        }
        List<SaveOrUpdateCrmDepartProductGroupRequest> saveRequestList=ListUtil.toList();
        List<SaveOrUpdateCrmDepartProductGroupRequest> updateRequestList=ListUtil.toList();
        List<Long> requestIdList=ListUtil.toList();
        requestList.forEach(request->{
            if(null==request.getId() || request.getId()==0){
                saveRequestList.add(request);
            }else {
                updateRequestList.add(request);
                requestIdList.add(request.getId());
            }
        });
        List<Long> deleteIdList = CollectionUtil.subtractToList(dataIdList, requestIdList);
        if(CollectionUtil.isNotEmpty(saveRequestList)){
            this.batchSave(saveRequestList,groupId,groupName,opUserId);
        }
        if(CollectionUtil.isNotEmpty(updateRequestList)){
            this.batchUpdate(updateRequestList,groupId,groupName,opUserId);
        }
        if(CollectionUtil.isNotEmpty(deleteIdList)){
            this.batchDelete(deleteIdList,opUserId);
        }
        return true;
    }

    private Boolean batchSave(List<SaveOrUpdateCrmDepartProductGroupRequest> requestList,Long groupId,String groupName, Long opUserId){
        List<CrmDepartmentProductRelationDO> saveList = requestList.stream().map(saveRequest -> {
            CrmDepartmentProductRelationDO saveDO = PojoUtils.map(saveRequest, CrmDepartmentProductRelationDO.class);
            saveDO.setOpUserId(opUserId);
            saveDO.setProductGroupId(groupId);
            saveDO.setProductGroup(groupName);
            return saveDO;
        }).collect(Collectors.toList());
        return this.saveBatch(saveList);
    }

    private Boolean batchDelete(List<Long> deleteIds, Long opUserId){
        LambdaQueryWrapper<CrmDepartmentProductRelationDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CrmDepartmentProductRelationDO::getId,deleteIds);
        CrmDepartmentProductRelationDO deleteDO = new CrmDepartmentProductRelationDO();
        deleteDO.setOpUserId(opUserId);
        this.batchDeleteWithFill(deleteDO,queryWrapper);
        return true;
    }

    private Boolean batchUpdate(List<SaveOrUpdateCrmDepartProductGroupRequest> requestList,Long groupId,String groupName, Long opUserId){
        List<CrmDepartmentProductRelationDO> updateList = requestList.stream().map(saveRequest -> {
            CrmDepartmentProductRelationDO updateDO = PojoUtils.map(saveRequest, CrmDepartmentProductRelationDO.class);
            updateDO.setProductGroupId(groupId);
            updateDO.setProductGroup(groupName);
            updateDO.setOpUserId(opUserId);
            return updateDO;
        }).collect(Collectors.toList());
        return this.updateBatchById(updateList);
    }
}
