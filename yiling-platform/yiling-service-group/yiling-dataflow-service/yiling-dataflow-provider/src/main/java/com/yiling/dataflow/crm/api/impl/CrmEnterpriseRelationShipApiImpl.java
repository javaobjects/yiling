package com.yiling.dataflow.crm.api.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseRelationShipPageListRequest;
import com.yiling.dataflow.agency.dto.request.SaveCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.agency.entity.CrmDepartmentAreaRelationDO;
import com.yiling.dataflow.agency.entity.CrmHospitalDO;
import com.yiling.dataflow.agency.entity.CrmPharmacyDO;
import com.yiling.dataflow.agency.entity.CrmSupplierDO;
import com.yiling.dataflow.agency.enums.AgencySupplyChainRoleEnum;
import com.yiling.dataflow.agency.service.CrmDepartmentAreaRelationService;
import com.yiling.dataflow.agency.service.CrmHospitalService;
import com.yiling.dataflow.agency.service.CrmPharmacyService;
import com.yiling.dataflow.agency.service.CrmSupplierService;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.crm.dto.CrmDepartmentAreaRelationDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationPostDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.request.ChangeRelationShipRequest;
import com.yiling.dataflow.crm.dto.request.RemoveCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/9/19
 */
@DubboService
@Slf4j
public class CrmEnterpriseRelationShipApiImpl implements CrmEnterpriseRelationShipApi {

    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;

    @Autowired
    private CrmHospitalService crmHospitalService;

    @Autowired
    private CrmDepartmentAreaRelationService crmDepartmentAreaRelationService;
    @Autowired
    private CrmSupplierService crmSupplierService;
    @Autowired
    private CrmPharmacyService crmPharmacyService;

    @Autowired
    private CrmGoodsGroupService crmGoodsGroupService;

    @Override
    public boolean isBreathingDepartmentByNameCode(List<String> nameCode) {
        return crmEnterpriseRelationShipService.isBreathingDepartmentByNameCode(nameCode);
    }

    @Override
    public List<CrmEnterpriseRelationShipDTO> getCrmEnterpriseRelationShipByNameList(List<String> nameList) {
        return crmEnterpriseRelationShipService.getCrmEnterpriseRelationShipByNameList(nameList);
    }

    @Override
    public Page<CrmEnterpriseRelationShipDTO> getCrmRelationPage(QueryCrmEnterpriseRelationShipPageListRequest request) {
        return crmHospitalService.getCrmRelationPage(request);
    }

    @Override
    public Boolean saveOrUpdate(SaveCrmEnterpriseRelationShipRequest request) {
        return crmHospitalService.saveOrUpdateRelationShip(request);
    }

    @Override
    public String getProvinceAreaByThreeParms( String yxDept, String yxProvince) {
        return  crmDepartmentAreaRelationService.getProvinceAreaByThreeParms(yxDept, yxProvince);
    }

    @Override
    public List<CrmDepartmentAreaRelationDTO> getGoodsGroup(String ywbm, Integer supplyChainRole) {
        return crmDepartmentAreaRelationService.getGoodsGroup(ywbm,supplyChainRole);
    }

    @Override
    public List<CrmEnterpriseRelationPostDTO> getEnterpriseRelationPostByProductGroup(List<Long> enterpriseIds) {
       //todo：获取人员信息
        QueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().select(CrmEnterpriseRelationShipDO::getId,
                        CrmEnterpriseRelationShipDO::getCrmEnterpriseId,
                        CrmEnterpriseRelationShipDO::getProductGroupId,
                        CrmEnterpriseRelationShipDO::getPostCode,
                        CrmEnterpriseRelationShipDO::getCategoryId,
                        CrmEnterpriseRelationShipDO::getManorId,
                        CrmEnterpriseRelationShipDO::getProductGroup)
                .in(CrmEnterpriseRelationShipDO::getCrmEnterpriseId,enterpriseIds);
        List<CrmEnterpriseRelationShipDO> list = crmEnterpriseRelationShipService.list(queryWrapper);
        // 用产品组id获取产品组名称,并赋值
        if(CollectionUtil.isNotEmpty(list)){
            List<Long> productGroupIds = list.stream().map(CrmEnterpriseRelationShipDO::getProductGroupId).distinct().collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(productGroupIds)){
                Map<Long, String> groupMap=new HashMap<>();
                List<CrmGoodsGroupDTO> group = crmGoodsGroupService.findGroupByIds(productGroupIds);
                if(CollectionUtil.isNotEmpty(group)){
                    groupMap = group.stream().collect(Collectors.toMap(CrmGoodsGroupDTO::getId, CrmGoodsGroupDTO::getName));
                }
                Map<Long, String> finalGroupMap = groupMap;
                list.forEach(item->{
                    if(StringUtils.isNotEmpty(finalGroupMap.get(item.getProductGroupId()))){
                        item.setProductGroup(finalGroupMap.get(item.getProductGroupId()));
                    }
                });
            }
        }
        return PojoUtils.map(list,CrmEnterpriseRelationPostDTO.class);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public Boolean unlockRelation(List<Long> srcRelationShipIps) {
        CrmEnterpriseRelationShipDO crmEnterpriseRelationShipDO = new CrmEnterpriseRelationShipDO();
        //最后一次解锁时间更新
        updateBatchLastUnlockTime(srcRelationShipIps);
        crmEnterpriseRelationShipDO.setDelFlag(1);
        QueryWrapper<CrmEnterpriseRelationShipDO> updateWrapper = new QueryWrapper<>();
        updateWrapper.lambda().in(CrmEnterpriseRelationShipDO::getId,srcRelationShipIps);
        crmEnterpriseRelationShipService.batchDeleteWithFill(crmEnterpriseRelationShipDO, updateWrapper);
        return true;
    }

    @Override
    public Boolean changeRelationShip(ChangeRelationShipRequest srcRelationShipIps) {
        return crmHospitalService.changeRelationShip(srcRelationShipIps);
    }

    @Override
    public Boolean batchUpdateById(List<CrmEnterpriseRelationShipDTO> updateData,String tableSuffix) {
        if(CollectionUtil.isNotEmpty(updateData)){
            List<CrmEnterpriseRelationShipDO> relationShipDOS = PojoUtils.map(updateData, CrmEnterpriseRelationShipDO.class);
            return crmEnterpriseRelationShipService.updateBackUpBatchById(relationShipDOS,tableSuffix);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public Boolean remove(RemoveCrmEnterpriseRelationShipRequest request) {
        CrmEnterpriseRelationShipDO relation = crmEnterpriseRelationShipService.getById(request.getId());
        request.setSubstituteRunning(relation.getSubstituteRunning());
        //最后解锁时间
        log.info("三者关系删除ID:{},角色",relation.getId(),relation.getSupplyChainRole());
        updateLastUnlockTime(relation);
        boolean flag= crmEnterpriseRelationShipService.remove(request);
        return flag;
    }

    @Override
    public CrmEnterpriseRelationShipDTO queryById(Long id) {
        return PojoUtils.map(crmEnterpriseRelationShipService.getById(id), CrmEnterpriseRelationShipDTO.class);
    }

    @Override
    public Long listSuffixByNameList(List<Long> ids, Long id, String tableSuffix) {
        Long relationShipDOS = crmEnterpriseRelationShipService.listSuffixByCrmEnterpriseIdList(ids, id, tableSuffix);
        return relationShipDOS;
    }

    @Override
    public List<CrmDepartmentAreaRelationDTO> getAllData() {
        List<CrmDepartmentAreaRelationDO> list = crmDepartmentAreaRelationService.list();
        return PojoUtils.map(list,CrmDepartmentAreaRelationDTO.class);
    }

    @Override
    public List<Long> getCrmEnterprisePostCode() {
        return crmEnterpriseRelationShipService.getCrmEnterprisePostCode();
    }

    @Override
    public List<Long> getCrmEnterIdListByPostCode(String postCode) {
        return crmEnterpriseRelationShipService.getCrmEnterIdListByPostCode(postCode);
    }

    @Override
    public Map<Long, Boolean> getUseByDepartRelationIds(List<Long> ids) {
        return crmEnterpriseRelationShipService.getUseByDepartRelationIds(ids);
    }

    @Override
    public List<CrmEnterpriseRelationShipDTO> getCrmEnterpriseRelationShipByCrmenterpriseIdList(List<Long> ids) {
        return crmEnterpriseRelationShipService.getCrmEnterpriseRelationShipByCrmenterpriseIdList(ids);
    }

    @Override
    public Long listSuffixByCrmEnterpriseIdList(List<Long> idList, Long crmEnterpriseId, String tableSuffix) {
        return crmEnterpriseRelationShipService.listSuffixByCrmEnterpriseIdList(idList,crmEnterpriseId,tableSuffix);
    }

    @Override
    public List<CrmEnterpriseRelationShipDTO> listSuffixByIdList(List<Long> idList, String tableSuffix) {
        return PojoUtils.map(crmEnterpriseRelationShipService.listSuffixByIdList(idList,tableSuffix),CrmEnterpriseRelationShipDTO.class);
    }

    @Override
    public Boolean getExistByCrmEnterpriseId(Long crmEnterpriseId) {
        return crmEnterpriseRelationShipService.getExistByCrmEnterpriseId(crmEnterpriseId);
    }
    public void updateBatchLastUnlockTime(List<Long> ids){
        if(CollUtil.isEmpty(ids)){
            log.info("三者关系解锁删除数据 标记解锁时间处理数据为空返回{}",ids);
            return;
        }
        List<CrmEnterpriseRelationShipDO> crmEnterpriseRelationShipDOS = crmEnterpriseRelationShipService.listByIds(ids);
        Map<Integer,List<CrmEnterpriseRelationShipDO>> roleIdMapCrmEnterIds=crmEnterpriseRelationShipDOS.stream().collect(Collectors.groupingBy(CrmEnterpriseRelationShipDO::getSupplyChainRoleType));
        for (Integer key:roleIdMapCrmEnterIds.keySet()){
            List<Long> crmEnterIds= Optional.ofNullable(roleIdMapCrmEnterIds.get(key).stream().map(CrmEnterpriseRelationShipDO::getCrmEnterpriseId).collect(Collectors.toList())).orElse(ListUtil.empty());
            log.info("三者关系解锁删除数据 标记解锁时间处理 key:{},crmEnterIds:{}",key,crmEnterIds);
            if(AgencySupplyChainRoleEnum.SUPPLIER==AgencySupplyChainRoleEnum.getByCode(key)&&CollUtil.isNotEmpty(crmEnterIds)){
                    UpdateWrapper<CrmSupplierDO> updateWrapper=new UpdateWrapper<>();
                    updateWrapper.lambda().set(CrmSupplierDO::getLastUnlockTime,new Date());
                    updateWrapper.lambda().in(CrmSupplierDO::getCrmEnterpriseId,crmEnterIds);
                    crmSupplierService.update(null,updateWrapper);
            }
            if(AgencySupplyChainRoleEnum.HOSPITAL==AgencySupplyChainRoleEnum.getByCode(key)&&CollUtil.isNotEmpty(crmEnterIds)){
                UpdateWrapper<CrmHospitalDO> updateHospitalWrapper=new UpdateWrapper<>();
                updateHospitalWrapper.lambda().set(CrmHospitalDO::getLastUnlockTime,new Date());
                updateHospitalWrapper.lambda().in(CrmHospitalDO::getCrmEnterpriseId,crmEnterIds);
                crmHospitalService.update(null,updateHospitalWrapper);
            }
            if(AgencySupplyChainRoleEnum.PHARMACY==AgencySupplyChainRoleEnum.getByCode(key)&&CollUtil.isNotEmpty(crmEnterIds)){
                UpdateWrapper<CrmPharmacyDO> updatePharmacyWrapper=new UpdateWrapper<>();
                updatePharmacyWrapper.lambda().set(CrmPharmacyDO::getLastUnlockTime,new Date());
                updatePharmacyWrapper.lambda().in(CrmPharmacyDO::getCrmEnterpriseId,crmEnterIds);
                crmPharmacyService.update(null,updatePharmacyWrapper);
            }
        }

    }
    public void updateLastUnlockTime(CrmEnterpriseRelationShipDO relation){
        if(AgencySupplyChainRoleEnum.SUPPLIER==AgencySupplyChainRoleEnum.getByCode(relation.getSupplyChainRoleType())){
            UpdateWrapper<CrmSupplierDO> updateWrapper=new UpdateWrapper<>();
            updateWrapper.lambda().set(CrmSupplierDO::getLastUnlockTime,new Date());
            updateWrapper.lambda().eq(CrmSupplierDO::getCrmEnterpriseId,relation.getCrmEnterpriseId());
            crmSupplierService.update(null,updateWrapper);
        }
        if(AgencySupplyChainRoleEnum.HOSPITAL==AgencySupplyChainRoleEnum.getByCode(relation.getSupplyChainRoleType())){
            UpdateWrapper<CrmHospitalDO> updateHospitalWrapper=new UpdateWrapper<>();
            updateHospitalWrapper.lambda().set(CrmHospitalDO::getLastUnlockTime,new Date());
            updateHospitalWrapper.lambda().eq(CrmHospitalDO::getCrmEnterpriseId,relation.getCrmEnterpriseId());
            crmHospitalService.update(null,updateHospitalWrapper);
        }
        if(AgencySupplyChainRoleEnum.PHARMACY==AgencySupplyChainRoleEnum.getByCode(relation.getSupplyChainRoleType())){
            UpdateWrapper<CrmPharmacyDO> updatePharmacyWrapper=new UpdateWrapper<>();
            updatePharmacyWrapper.lambda().set(CrmPharmacyDO::getLastUnlockTime,new Date());
            updatePharmacyWrapper.lambda().eq(CrmPharmacyDO::getCrmEnterpriseId,relation.getCrmEnterpriseId());
            crmPharmacyService.update(null,updatePharmacyWrapper);
        }
    }
}
