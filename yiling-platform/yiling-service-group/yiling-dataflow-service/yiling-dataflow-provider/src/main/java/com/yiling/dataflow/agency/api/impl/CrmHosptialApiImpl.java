package com.yiling.dataflow.agency.api.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.api.CrmHosptialApi;
import com.yiling.dataflow.agency.dto.CrmHospitalDTO;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyPageListRequest;
import com.yiling.dataflow.agency.dto.request.SaveCrmHosptialRequest;
import com.yiling.dataflow.agency.dto.request.SaveCrmRelationshipRequest;
import com.yiling.dataflow.agency.dto.request.UpdateCrmHospitalRequest;
import com.yiling.dataflow.agency.entity.CrmHospitalDO;
import com.yiling.dataflow.agency.service.CrmHospitalService;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.enums.CrmSupplyChainRoleEnum;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 医疗机构档案扩展表 API
 *
 * @author: shixing.sun
 * @date: 2023/2/14
 */
@DubboService
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CrmHosptialApiImpl implements CrmHosptialApi {

    private final CrmHospitalService crmHospitalService;

    private final CrmEnterpriseRelationShipService relationShipService;

    private final CrmEnterpriseService crmEnterpriseService;

    private final CrmGoodsGroupService crmGoodsGroupService;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @Override
    public Page<CrmHospitalDTO> pageList(QueryCrmAgencyPageListRequest request) {
        return null;
    }

    @Override
    public List<CrmHospitalDTO> getHosptialInfoByCrmEnterId(List<Long> crmEnterIds) {
        QueryWrapper<CrmHospitalDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CrmHospitalDO::getCrmEnterpriseId, crmEnterIds);
        List<CrmHospitalDO> list = crmHospitalService.list(queryWrapper);
        return PojoUtils.map(list, CrmHospitalDTO.class);
    }

    @Override
    public Boolean saveEnterpriseInfoAndHosptialInfo(SaveCrmHosptialRequest request) {
        CrmHospitalDO crmHospitalDO = PojoUtils.map(request, CrmHospitalDO.class);
        // 新增可以编辑，编辑只能新增。
        // 有id的不管，没id的新增
        List<SaveCrmRelationshipRequest> crmRelationShip = request.getCrmRelationShip();
        List<SaveCrmRelationshipRequest> saveCrmRelationshipRequestList = Optional.ofNullable( crmRelationShip.stream().filter(item -> ObjectUtil.isNull(item.getId())).collect(Collectors.toList())).orElse(ListUtil.empty());
        //商业首次锁定时间 2个list相等的情况>0并且长度相等
        if(CollUtil.isNotEmpty(crmRelationShip)&&CollUtil.isNotEmpty(saveCrmRelationshipRequestList)&&saveCrmRelationshipRequestList.size()==crmRelationShip.size()){
            crmHospitalDO.setFirstLockTime(new Date());
        }
        if (ObjectUtil.isEmpty(crmHospitalDO.getId())) {
            crmHospitalService.save(crmHospitalDO);
        } else {
            crmHospitalService.updateById(crmHospitalDO);
        }
        if (CollectionUtils.isEmpty(request.getCrmRelationShip())||CollectionUtils.isEmpty(saveCrmRelationshipRequestList)) {
            return true;
        }
        saveCrmRelationshipRequestList.forEach(item -> {
            item.setCustomerName(request.getName());
            // 1 供应商 2 医院 3 药店
            item.setSupplyChainRole(CrmSupplyChainRoleEnum.HOSPITAL.getName());
            item.setSupplyChainRoleType(CrmSupplyChainRoleEnum.HOSPITAL.getCode());
            item.setOpUserId(request.getOpUserId());
            if (ObjectUtil.isNotEmpty(request.getCrmEnterpriseId())) {
                item.setCrmEnterpriseId(request.getCrmEnterpriseId());
            }
        });
        //医院类型基础数据操作的时候去掉三者关系的变更-DIH-237需求
        return true;
    }

    @Override
    public CrmHospitalDTO getCrmSupplierByCrmEnterId(String id) {
        CrmHospitalDO crmHospitalDO = crmHospitalService.getByCrmEnterpriseId(Long.parseLong(id));
        return PojoUtils.map(crmHospitalDO, CrmHospitalDTO.class);
    }

    @Override
    public void removeCrmSupplierById(String id) {
        //基本信息更新,暂时不删除三者关系表
        UpdateWrapper<CrmEnterpriseDO> updateWrapper1 = new UpdateWrapper<>();
        updateWrapper1.lambda().set(CrmEnterpriseDO::getDelFlag, 1).eq(CrmEnterpriseDO::getId, id);
        crmEnterpriseService.update(updateWrapper1);
        //扩展表更新
        UpdateWrapper<CrmHospitalDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(CrmHospitalDO::getDelFlag, 1).eq(CrmHospitalDO::getCrmEnterpriseId, id);
        crmHospitalService.update(updateWrapper);
    }

    @Override
    public List<CrmEnterpriseRelationShipDTO> getRelationByCrmEnterpriseId(Long id, String name) {
        LambdaQueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CrmEnterpriseRelationShipDO::getCrmEnterpriseId, id);
        List<CrmEnterpriseRelationShipDO> list = relationShipService.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            List<Long> postIds = list.stream().map(CrmEnterpriseRelationShipDO::getPostCode).collect(Collectors.toList());
            List<EsbEmployeeDTO> result = esbEmployeeApi.listByJobIdsForAgency(postIds);
            if (CollectionUtils.isNotEmpty(result)) {
                Map<Long, EsbEmployeeDTO> esbEmployeeDTOMap = result.stream().collect(Collectors.toMap(EsbEmployeeDTO::getJobId, Function.identity()));
                list.stream().forEach(item -> {
                    EsbEmployeeDTO esbEmployeeDTO = esbEmployeeDTOMap.get(item.getPostCode());
                    if (ObjectUtil.isNotEmpty(esbEmployeeDTO)) {
                        item.setRepresentativeCode(esbEmployeeDTO.getEmpId());
                        item.setRepresentativeName(esbEmployeeDTO.getEmpName());
                    }
                });
            }
            // 用产品组id获取产品组名称,并赋值
            List<Long> productGroupIds = list.stream().map(CrmEnterpriseRelationShipDO::getProductGroupId).distinct().collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(productGroupIds)) {
                Map<Long, String> groupMap = new HashMap<>();
                List<CrmGoodsGroupDTO> group = crmGoodsGroupService.findGroupByIds(productGroupIds);
                if (CollectionUtil.isNotEmpty(group)) {
                    groupMap = group.stream().collect(Collectors.toMap(CrmGoodsGroupDTO::getId, CrmGoodsGroupDTO::getName));
                }
                Map<Long, String> finalGroupMap = groupMap;
                list.forEach(item -> {
                    if (StringUtils.isNotEmpty(finalGroupMap.get(item.getProductGroupId()))) {
                        item.setProductGroup(finalGroupMap.get(item.getProductGroupId()));
                    }
                });
            }

        }
        return PojoUtils.map(list, CrmEnterpriseRelationShipDTO.class);
    }

    @Override
    public boolean updateCrmHospitalBatch(List<UpdateCrmHospitalRequest> requests) {
        if (CollUtil.isEmpty(requests)){
            return true;
        }
        List<CrmHospitalDO> list = PojoUtils.map(requests,CrmHospitalDO.class);
        LambdaQueryWrapper<CrmHospitalDO> queryWrapper = Wrappers.lambdaQuery();
        List<Long> enterpriseIdList = requests.stream().map(UpdateCrmHospitalRequest::getCrmEnterpriseId).collect(Collectors.toList());
        if(CollUtil.isEmpty(enterpriseIdList)){
            return false;
        }
        queryWrapper.in(CrmHospitalDO::getCrmEnterpriseId,enterpriseIdList);
        List<CrmHospitalDO> crmSupplierDOList = crmHospitalService.list(queryWrapper);
        Map<Long, Long> collect = crmSupplierDOList.stream().collect(Collectors.toMap(CrmHospitalDO::getCrmEnterpriseId, CrmHospitalDO::getId));
        list.forEach(crmSupplierDO -> {
            crmSupplierDO.setId(collect.get(crmSupplierDO.getCrmEnterpriseId()));
        });
        return crmHospitalService.saveOrUpdateBatch(list);
    }


}
