package com.yiling.dataflow.agency.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.collection.ListUtil;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.framework.common.annotations.DynamicName;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.dataflow.agency.api.CrmSupplierApi;
import com.yiling.dataflow.agency.dto.CrmEnterpriseNameDTO;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.dataflow.agency.dto.request.RemoveCrmSupplierRequest;
import com.yiling.dataflow.agency.dto.request.SaveCrmRelationshipRequest;
import com.yiling.dataflow.agency.dto.request.SaveCrmSupplierRequest;
import com.yiling.dataflow.agency.dto.request.UpdateCrmSupplierRequest;
import com.yiling.dataflow.agency.entity.CrmSupplierDO;
import com.yiling.dataflow.agency.service.CrmSupplierService;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;

@DubboService
public class CrmSupplierApiImpl implements CrmSupplierApi
{
    @Autowired
    private  CrmSupplierService crmSupplierService;
    @Autowired
    private CrmEnterpriseService crmEnterpriseService;
    @Autowired
    private  CrmEnterpriseRelationShipService relationShipService;

    @Override
    public List<CrmSupplierDTO> getSupplierInfoByCrmEnterId(List<Long> crmEnterIds) {
        return crmSupplierService.getSupplierInfoByCrmEnterId(crmEnterIds);
    }

    @Override
    @Transactional(rollbackFor ={Exception.class, RuntimeException.class} )
    public void removeCrmSupplierById(RemoveCrmSupplierRequest  request) {
        //基本信息更新

        UpdateWrapper<CrmEnterpriseDO> updateWrapper1=new UpdateWrapper<>();
        updateWrapper1.lambda()
                .set(CrmEnterpriseDO::getDelFlag,1)
                .set(CrmEnterpriseDO::getUpdateUser,request.getOpUserId())
                .set(CrmEnterpriseDO::getUpdateTime,request.getOpTime())
                .eq(CrmEnterpriseDO::getId,request.getCrmEnterpriseId());
        crmEnterpriseService.update(updateWrapper1);
        //扩展表更新
        UpdateWrapper<CrmSupplierDO> updateWrapper=new UpdateWrapper<>();
        updateWrapper.lambda()
                .set(CrmSupplierDO::getDelFlag,1)
                .set(CrmSupplierDO::getUpdateUser,request.getOpUserId())
                .set(CrmSupplierDO::getUpdateTime,request.getOpTime())
                .eq(CrmSupplierDO::getCrmEnterpriseId,request.getCrmEnterpriseId());
        crmSupplierService.update(updateWrapper);
    }

    @Override
    public CrmSupplierDTO getCrmSupplierByCrmEnterId(Long id) {
        QueryWrapper<CrmSupplierDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CrmSupplierDO::getDelFlag,0).eq(CrmSupplierDO::getCrmEnterpriseId, id);
        CrmSupplierDO supplierDO=  crmSupplierService.getOne(queryWrapper);
        return PojoUtils.map(supplierDO,CrmSupplierDTO.class);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public CrmSupplierDTO getBakCrmSupplierByCrmEnterId(Long id, String tableSuffix) {
        QueryWrapper<CrmSupplierDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CrmSupplierDO::getDelFlag,0).eq(CrmSupplierDO::getCrmEnterpriseId, id);
        CrmSupplierDO supplierDO=  crmSupplierService.getOne(queryWrapper);
        return PojoUtils.map(supplierDO,CrmSupplierDTO.class);
    }

    @Override
    @Transactional(rollbackFor ={Exception.class, RuntimeException.class} )
    public int saveCrmSupplierInfo(SaveCrmSupplierRequest request) {
        CrmSupplierDO supplierDO=PojoUtils.map(request,CrmSupplierDO.class);
        supplierDO.setCreateTime(request.getOpTime());
        supplierDO.setCreateUser(request.getOpUserId());

        // 有id的不管，没id的新增
        List<SaveCrmRelationshipRequest> crmRelationShip = request.getCrmRelationShip();
        List<SaveCrmRelationshipRequest> saveCrmRelationshipRequestList = Optional.ofNullable(crmRelationShip.stream().filter(item -> ObjectUtil.isNull(item.getId())).collect(Collectors.toList())).orElse(ListUtil.empty());
        //商业首次锁定时间 2个list相等的情况>0并且长度相等
        if(CollUtil.isNotEmpty(crmRelationShip)&&CollUtil.isNotEmpty(saveCrmRelationshipRequestList)&&saveCrmRelationshipRequestList.size()==crmRelationShip.size()){
            supplierDO.setFirstLockTime(new Date());
        }
        crmSupplierService.save(supplierDO);
        if (CollectionUtils.isEmpty(request.getCrmRelationShip())) {
            return 1;
        }
        if (CollectionUtils.isEmpty(saveCrmRelationshipRequestList)) {
            return 1;
        }
        // 先删除，再新增三者关系
        saveCrmRelationshipRequestList.forEach(item -> {
            if(StringUtils.equals(item.getDutyGredeId(),"2")){
                item.setPostCode(item.getSuperiorJob());
            }
            item.setOpUserId(request.getOpUserId());
            item.setCustomerName(request.getName());
            // 1 供应商 2 医院 3 药店
            item.setSupplyChainRole(CrmSupplyChainRoleEnum.DISTRIBUTOR.getName());
            item.setSupplyChainRoleType(CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode());
            if (ObjectUtil.isNotEmpty(request.getCrmEnterpriseId())) {
                item.setCrmEnterpriseId(request.getCrmEnterpriseId());
            }
        });
        relationShipService.saveBatch(PojoUtils.map(saveCrmRelationshipRequestList, CrmEnterpriseRelationShipDO.class));
        return 0;
    }

    @Override
    public boolean updateCrmSupplierBatch(List<UpdateCrmSupplierRequest> requests) {
        if (CollUtil.isEmpty(requests)){
            return true;
        }
        List<CrmSupplierDO> list = PojoUtils.map(requests,CrmSupplierDO.class);
        LambdaQueryWrapper<CrmSupplierDO> queryWrapper = Wrappers.lambdaQuery();
        List<Long> enterpriseIdList = requests.stream().map(UpdateCrmSupplierRequest::getCrmEnterpriseId).collect(Collectors.toList());
        if(CollUtil.isEmpty(enterpriseIdList)){
            return false;
        }
        queryWrapper.in(CrmSupplierDO::getCrmEnterpriseId,enterpriseIdList);
        List<CrmSupplierDO> crmSupplierDOList = crmSupplierService.list(queryWrapper);
        Map<Long, Long> collect = crmSupplierDOList.stream().collect(Collectors.toMap(CrmSupplierDO::getCrmEnterpriseId, CrmSupplierDO::getId));
        list.forEach(crmSupplierDO -> {
            crmSupplierDO.setId(collect.get(crmSupplierDO.getCrmEnterpriseId()));
        });
        return crmSupplierService.saveOrUpdateBatch(list);
    }

    @Override
    @Transactional(rollbackFor ={Exception.class, RuntimeException.class} )
    public int updateCrmSupplierInfo(UpdateCrmSupplierRequest request) {
        CrmSupplierDO supplierDO=PojoUtils.map(request,CrmSupplierDO.class);
        // 有id的不管，没id的新增
        List<SaveCrmRelationshipRequest> crmRelationShip = request.getCrmRelationShip();
        List<SaveCrmRelationshipRequest> saveCrmRelationshipRequestList = Optional.ofNullable(crmRelationShip.stream().filter(item -> ObjectUtil.isNull(item.getId())).collect(Collectors.toList())).orElse(ListUtil.empty());
        supplierDO.setUpdateTime(request.getOpTime());
        supplierDO.setUpdateUser(request.getOpUserId());
        UpdateWrapper<CrmSupplierDO> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("crm_enterprise_id",request.getCrmEnterpriseId());
        //商业首次锁定时间 2个list相等的情况>0并且长度相等
        if(CollUtil.isNotEmpty(crmRelationShip)&&CollUtil.isNotEmpty(saveCrmRelationshipRequestList)&&saveCrmRelationshipRequestList.size()==crmRelationShip.size()){
            supplierDO.setFirstLockTime(new Date());
        }
        boolean result = crmSupplierService.saveOrUpdate(supplierDO,updateWrapper);
        if (CollectionUtils.isEmpty(saveCrmRelationshipRequestList)) {
            return 1;
        }
        // 先删除，再新增三者关系
        saveCrmRelationshipRequestList.forEach(item -> {
            if(StringUtils.equals(item.getDutyGredeId(),"2")){
                item.setPostCode(item.getSuperiorJob());
            }
            item.setCustomerName(request.getName());
            // 1 供应商 2 医院 3 药店
            item.setSupplyChainRole("1");
            if (ObjectUtil.isNotEmpty(request.getCrmEnterpriseId())) {
                item.setCrmEnterpriseId(request.getCrmEnterpriseId());
            }
        });
        relationShipService.saveBatch(PojoUtils.map(saveCrmRelationshipRequestList, CrmEnterpriseRelationShipDO.class));
        return 1;
    }

    @Override
    public List<CrmEnterpriseRelationShipDTO> getRelationByCrmEnterpriseId(Long id, String name) {
        LambdaQueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CrmEnterpriseRelationShipDO::getCrmEnterpriseId, id);
        return PojoUtils.map(relationShipService.list(queryWrapper), CrmEnterpriseRelationShipDTO.class);
    }

    @Override
    public List<CrmEnterpriseNameDTO> getCrmEnterpriseNameListById(List<String> parentCrmEenterIds) {
        LambdaQueryWrapper<CrmEnterpriseDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(CrmEnterpriseDO::getId,CrmEnterpriseDO::getName);
        queryWrapper.in(CrmEnterpriseDO::getId,parentCrmEenterIds);
        return PojoUtils.map(crmEnterpriseService.list(queryWrapper),CrmEnterpriseNameDTO.class);
    }

    @Override
    public List<Long> listByLevelAndGroup(Integer supplierLevel, Integer businessSystem) {
        List<Long> crmEnterIds = crmSupplierService.listByLevelAndGroup(supplierLevel, businessSystem);
        return crmEnterIds;
    }

    @Override
    public List<Long> getCrmEnterIdListByCommerceJobNumber(String empId) {
        return crmSupplierService.getCrmEnterIdListByCommerceJobNumber(empId);
    }
}
