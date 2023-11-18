package com.yiling.dataflow.agency.service.impl;

import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dao.CrmHospitalMapper;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseByNamePageListRequest;
import com.yiling.dataflow.agency.dto.request.QueryCrmEnterpriseRelationShipPageListRequest;
import com.yiling.dataflow.agency.dto.request.SaveCrmEnterpriseRelationShipRequest;
import com.yiling.dataflow.agency.entity.CrmHospitalDO;
import com.yiling.dataflow.agency.service.CrmHospitalService;
import com.yiling.dataflow.crm.api.CrmGoodsGroupApi;
import com.yiling.dataflow.crm.dao.CrmEnterpriseRelationShipMapper;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsGroupDTO;
import com.yiling.dataflow.crm.dto.request.ChangeRelationShipDetailRequest;
import com.yiling.dataflow.crm.dto.request.ChangeRelationShipRequest;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.entity.CrmEnterpriseRelationShipDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseRelationShipService;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.crm.service.CrmGoodsGroupService;
import com.yiling.framework.common.annotations.DynamicName;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 医疗机构拓展表 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2023-02-14
 */
@Service
@Slf4j
public class CrmHospitalServiceImpl extends BaseServiceImpl<CrmHospitalMapper, CrmHospitalDO> implements CrmHospitalService {

    @Autowired
    private CrmEnterpriseRelationShipMapper crmEnterpriseRelationShipMapper;

    @Autowired
    private CrmEnterpriseRelationShipService crmEnterpriseRelationShipService;

    @Autowired
    private CrmEnterpriseService enterpriseService;

    @Autowired
    CrmGoodsGroupService crmGoodsGroupService;


    @Override
    public List<CrmHospitalDO> getByCrmEnterpriseIds(List<Long> ids) {
        QueryWrapper<CrmHospitalDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(CrmHospitalDO::getCrmEnterpriseId, ids);
        queryWrapper.lambda().eq(CrmHospitalDO::getDelFlag, 0);

        return this.getBaseMapper().selectList(queryWrapper);
    }

    @Override
    public CrmHospitalDO getByCrmEnterpriseId(Long id) {
        QueryWrapper<CrmHospitalDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CrmHospitalDO::getCrmEnterpriseId, id);
        queryWrapper.lambda().eq(CrmHospitalDO::getDelFlag, 0);
        return this.getOne(queryWrapper);
    }

    @Override
    public Page<CrmEnterpriseRelationShipDTO> getCrmRelationPage(QueryCrmEnterpriseRelationShipPageListRequest request) {
        LambdaQueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new LambdaQueryWrapper<>();
        /**
         * 品种ID
         */
        if (ObjectUtil.isNotEmpty(request.getCategoryId())){
            queryWrapper.eq(CrmEnterpriseRelationShipDO::getCategoryId, request.getCategoryId());
        }
        if (ObjectUtil.isNotEmpty(request.getPostCode())){
            queryWrapper.eq(CrmEnterpriseRelationShipDO::getPostCode, request.getPostCode());
        }
        if (StringUtils.isNotEmpty(request.getBusinessDepartment())) {
            queryWrapper.likeRight(CrmEnterpriseRelationShipDO::getBusinessDepartment, request.getBusinessDepartment());
        }
        if (StringUtils.isNotEmpty(request.getRepresentativeName())) {
            queryWrapper.eq(CrmEnterpriseRelationShipDO::getRepresentativeName, request.getRepresentativeName());
        }
        if (CollectionUtil.isNotEmpty(request.getCrmEnterpriseIds())) {
            queryWrapper.in(CrmEnterpriseRelationShipDO::getCrmEnterpriseId, request.getCrmEnterpriseIds());
        }
        if (ObjectUtil.isNotEmpty(request.getCrmEnterpriseId())) {
            queryWrapper.eq(CrmEnterpriseRelationShipDO::getCrmEnterpriseId, request.getCrmEnterpriseId());
        }
        if (StringUtils.isNotEmpty(request.getCustomerName())) {
            queryWrapper.like(CrmEnterpriseRelationShipDO::getCustomerName, request.getCustomerName());
        }
        if (ObjectUtil.isNotEmpty(request.getSupplyChainRole())) {
            queryWrapper.eq(CrmEnterpriseRelationShipDO::getSupplyChainRoleType, request.getSupplyChainRole());
        }
        if (ObjectUtil.isNotEmpty(request.getBeginTime())) {
            queryWrapper.ge(CrmEnterpriseRelationShipDO::getCreateTime, DateUtil.parse(DateUtil.format(request.getBeginTime(), "yyyy-MM-dd 00:00:00")));
        }
        if (ObjectUtil.isNotEmpty(request.getEndTime())) {
            queryWrapper.le(CrmEnterpriseRelationShipDO::getCreateTime, DateUtil.parse(DateUtil.format(request.getEndTime(), "yyyy-MM-dd 23:59:59")));
        }
        if(ObjectUtil.isNotNull(request.getSjmsUserDatascopeBO())&&OrgDatascopeEnum.PORTION==OrgDatascopeEnum.getFromCode(request.getSjmsUserDatascopeBO().getOrgDatascope())){
            SjmsUserDatascopeBO sjmsUserDatascopeBO= request.getSjmsUserDatascopeBO();
            //省区和crmEnterIds2个都不为空的时候时或的关系
            if (OrgDatascopeEnum.PORTION==OrgDatascopeEnum.getFromCode(sjmsUserDatascopeBO.getOrgDatascope())){
                List<Long> crmEnterIds = enterpriseService.getCrmEnterpriseListByEidsAndProvinceCode(sjmsUserDatascopeBO.getOrgPartDatascopeBO().getCrmEids(), sjmsUserDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes());
                queryWrapper.in(CrmEnterpriseRelationShipDO::getCrmEnterpriseId,CollUtil.isNotEmpty(crmEnterIds)?crmEnterIds: Arrays.asList(-1L));
            }
        }
        queryWrapper.orderByDesc(CrmEnterpriseRelationShipDO::getId);
        Page<CrmEnterpriseRelationShipDO> list = crmEnterpriseRelationShipMapper.selectPage(request.getPage(), queryWrapper);
        // 用产品组id获取产品组名称,并赋值
        if(CollectionUtil.isNotEmpty(list.getRecords())){
            List<Long> productGroupIds = list.getRecords().stream().map(CrmEnterpriseRelationShipDO::getProductGroupId).distinct().collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(productGroupIds)){
                Map<Long, String> groupMap=new HashMap<>();
                List<CrmGoodsGroupDTO> group = crmGoodsGroupService.findGroupByIds(productGroupIds);
                if(CollectionUtil.isNotEmpty(group)){
                    groupMap = group.stream().collect(Collectors.toMap(CrmGoodsGroupDTO::getId, CrmGoodsGroupDTO::getName));
                }
                Map<Long, String> finalGroupMap = groupMap;
                list.getRecords().forEach(item->{
                    if(StringUtils.isNotEmpty(finalGroupMap.get(item.getProductGroupId()))){
                        item.setProductGroup(finalGroupMap.get(item.getProductGroupId()));
                    }
                });
            }
        }
        return PojoUtils.map(list, CrmEnterpriseRelationShipDTO.class);
    }

    @Override
    public Boolean saveOrUpdateRelationShip(SaveCrmEnterpriseRelationShipRequest request) {
        return crmEnterpriseRelationShipService.saveOrUpdate(PojoUtils.map(request, CrmEnterpriseRelationShipDO.class));
    }


    @Override
    public Boolean changeRelationShip(ChangeRelationShipRequest srcRelationShipIps) {
        List<ChangeRelationShipDetailRequest> requestList = srcRelationShipIps.getRequestList();
        if (CollectionUtil.isEmpty(requestList)) {
            return false;
        }
        requestList.forEach(item -> {
            if (CollectionUtil.isNotEmpty(item.getIds()) && ObjectUtil.isNotEmpty(item.getPostCode()) && StringUtils.isNotEmpty(item.getPostName())) {
                CrmEnterpriseRelationShipDO crmEnterpriseRelationShipDO = new CrmEnterpriseRelationShipDO();
                crmEnterpriseRelationShipDO.setPostCode(item.getPostCode());
                crmEnterpriseRelationShipDO.setPostName(item.getPostName());
                UpdateWrapper<CrmEnterpriseRelationShipDO> objectUpdateWrapper = new UpdateWrapper<>();
                objectUpdateWrapper.lambda().in(CrmEnterpriseRelationShipDO::getId,item.getIds());
                objectUpdateWrapper.lambda().eq(CrmEnterpriseRelationShipDO::getDelFlag,0);
                crmEnterpriseRelationShipService.update(crmEnterpriseRelationShipDO,objectUpdateWrapper);
            }
        });
        return true;
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public List<CrmHospitalDO> listSuffixByCrmEnterpriseIdList(List<Long> crmEnterpriseIdList, String tableSuffix) {
        LambdaQueryWrapper<CrmHospitalDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CrmHospitalDO::getCrmEnterpriseId, crmEnterpriseIdList);
        return this.list(queryWrapper);
    }

    @Override
    @DynamicName(spel = "#tableSuffix")
    public Page<CrmEnterpriseRelationShipDTO> getCrmRelationBackUpPage(QueryCrmEnterpriseRelationShipPageListRequest request, String tableSuffix) {
        LambdaQueryWrapper<CrmEnterpriseRelationShipDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(CrmEnterpriseRelationShipDO::getId);
        return PojoUtils.map(crmEnterpriseRelationShipMapper.selectPage(request.getPage(), queryWrapper),CrmEnterpriseRelationShipDTO.class);
    }
}
