package com.yiling.dataflow.flow.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flow.bo.FlowEnterpriseConnectStatisticBO;
import com.yiling.dataflow.flow.dao.FlowEnterpriseConnectMonitorMapper;
import com.yiling.dataflow.flow.dto.FlowEnterpriseConnectMonitorDTO;
import com.yiling.dataflow.flow.dto.request.QueryEnterpriseConnectMonitorPageRequest;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseConnectMonitorRequest;
import com.yiling.dataflow.flow.entity.FlowEnterpriseConnectMonitorDO;
import com.yiling.dataflow.flow.entity.FlowPurchaseChannelDO;
import com.yiling.dataflow.flow.enums.ConnectStatusEnum;
import com.yiling.dataflow.flow.service.FlowEnterpriseConnectMonitorService;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowEnterpriseConnectMonitorServiceImpl
 * @描述
 * @创建时间 2023/3/27
 * @修改人 shichen
 * @修改时间 2023/3/27
 **/
@Service
@Slf4j
public class FlowEnterpriseConnectMonitorServiceImpl extends BaseServiceImpl<FlowEnterpriseConnectMonitorMapper, FlowEnterpriseConnectMonitorDO> implements FlowEnterpriseConnectMonitorService {
    @Override
    public Long saveOrUpdate(SaveFlowEnterpriseConnectMonitorRequest request) {
        log.info("新增或修改流向直连监控数据参数:{}", JSON.toJSONString(request));
        if(null==request.getErpClientId() || request.getErpClientId()==0){
            throw new BusinessException(ResultCode.FAILED,"erp客户端id为空");
        }
        if(null==request.getCrmEnterpriseId() || request.getCrmEnterpriseId()==0){
            throw new BusinessException(ResultCode.FAILED,"经销商id为空");
        }
        FlowEnterpriseConnectMonitorDO saveOrUpdateDO = PojoUtils.map(request, FlowEnterpriseConnectMonitorDO.class);
        if(null==saveOrUpdateDO.getId() || saveOrUpdateDO.getId()==0){
            FlowEnterpriseConnectMonitorDTO monitorDTO = this.findByErpClientId(request.getErpClientId());
            if(null!=monitorDTO){
                saveOrUpdateDO.setId(monitorDTO.getId());
                this.updateById(saveOrUpdateDO);
                return monitorDTO.getId();
            }
            this.save(saveOrUpdateDO);
            return saveOrUpdateDO.getId();
        }
        this.updateById(saveOrUpdateDO);
        return saveOrUpdateDO.getId();
    }

    @Override
    public FlowEnterpriseConnectMonitorDTO findByCrmEid(Long crmEnterpriseId) {
        QueryWrapper<FlowEnterpriseConnectMonitorDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(FlowEnterpriseConnectMonitorDO::getCrmEnterpriseId,crmEnterpriseId);
        queryWrapper.lambda().last("limit 1");
        return PojoUtils.map(this.getOne(queryWrapper),FlowEnterpriseConnectMonitorDTO.class);
    }

    @Override
    public FlowEnterpriseConnectMonitorDTO findByErpClientId(Long erpClientId) {
        QueryWrapper<FlowEnterpriseConnectMonitorDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(FlowEnterpriseConnectMonitorDO::getErpClientId,erpClientId);
        queryWrapper.lambda().last("limit 1");
        return PojoUtils.map(this.getOne(queryWrapper),FlowEnterpriseConnectMonitorDTO.class);
    }

    @Override
    public Page<FlowEnterpriseConnectMonitorDTO> page(QueryEnterpriseConnectMonitorPageRequest request) {
        LambdaQueryWrapper<FlowEnterpriseConnectMonitorDO> lambdaWrapper = Wrappers.lambdaQuery();
        if(StringUtils.isNotBlank(request.getCrmEnterpriseName())){
            lambdaWrapper.like(FlowEnterpriseConnectMonitorDO::getCrmEnterpriseName,request.getCrmEnterpriseName());
        }
        if(null != request.getCrmEnterpriseId() && request.getCrmEnterpriseId()>0){
            lambdaWrapper.eq(FlowEnterpriseConnectMonitorDO::getCrmEnterpriseId,request.getCrmEnterpriseId());
        }
        if(null!= request.getSupplierLevel() && request.getSupplierLevel()>0){
            lambdaWrapper.eq(FlowEnterpriseConnectMonitorDO::getSupplierLevel,request.getSupplierLevel());
        }
        if(null!= request.getFlowMode() && request.getFlowMode()>0){
            lambdaWrapper.eq(FlowEnterpriseConnectMonitorDO::getFlowMode,request.getFlowMode());
        }
        if(null!= request.getConnectStatus()){
            lambdaWrapper.eq(FlowEnterpriseConnectMonitorDO::getConnectStatus,request.getConnectStatus());
        }
        if(null !=request.getStartDockingTime() && null!= request.getEndDockingTime()){
            lambdaWrapper.between(FlowEnterpriseConnectMonitorDO::getDockingTime,request.getStartDockingTime(),request.getEndDockingTime());
        }
        if(null !=request.getStartCollectionTime() && null!= request.getEndCollectionTime()){
            lambdaWrapper.between(FlowEnterpriseConnectMonitorDO::getFlowCollectionTime,request.getStartCollectionTime(),request.getEndCollectionTime());
        }
        if(null !=request.getStartFlowDayCount()){
            lambdaWrapper.ge(FlowEnterpriseConnectMonitorDO::getReturnFlowDayCount,request.getStartFlowDayCount());
        }
        if(null!= request.getEndFlowDayCount()){
            lambdaWrapper.le(FlowEnterpriseConnectMonitorDO::getReturnFlowDayCount,request.getEndFlowDayCount());
        }
        //权限相关参数
        if(null != request.getUserDatascopeBO() && OrgDatascopeEnum.PORTION.getCode().equals(request.getUserDatascopeBO().getOrgDatascope())){
            List<Long> crmEids = request.getUserDatascopeBO().getOrgPartDatascopeBO().getCrmEids();
            List<String> provinceCodes = request.getUserDatascopeBO().getOrgPartDatascopeBO().getProvinceCodes();
            if(CollectionUtil.isNotEmpty(crmEids) && CollectionUtil.isNotEmpty(provinceCodes)){
                return new Page<>(request.getCurrent(),request.getSize());
            }
            lambdaWrapper.and(wrapper->{
                if(CollectionUtil.isNotEmpty(crmEids)){
                    wrapper.in(FlowEnterpriseConnectMonitorDO::getCrmEnterpriseId,crmEids);
                    if(CollectionUtil.isNotEmpty(provinceCodes)){
                        wrapper.or().in(FlowEnterpriseConnectMonitorDO::getProvinceCode,provinceCodes);
                    }
                }else if(CollectionUtil.isNotEmpty(provinceCodes)){
                    wrapper.in(FlowEnterpriseConnectMonitorDO::getProvinceCode,provinceCodes);
                }
            });
        }
        lambdaWrapper.orderByDesc(FlowEnterpriseConnectMonitorDO::getReturnFlowDayCount);
        Page<FlowEnterpriseConnectMonitorDO> page = this.page(request.getPage(), lambdaWrapper);
        return PojoUtils.map(page,FlowEnterpriseConnectMonitorDTO.class);
    }

    @Override
    public List<FlowEnterpriseConnectMonitorDTO> listByQuery(QueryEnterpriseConnectMonitorPageRequest request) {
        LambdaQueryWrapper<FlowEnterpriseConnectMonitorDO> lambdaWrapper = Wrappers.lambdaQuery();
        if(null != request.getCrmEnterpriseId() && request.getCrmEnterpriseId()>0){
            lambdaWrapper.eq(FlowEnterpriseConnectMonitorDO::getCrmEnterpriseId,request.getCrmEnterpriseId());
        }
        if(StringUtils.isNotBlank(request.getCrmEnterpriseName())){
            lambdaWrapper.like(FlowEnterpriseConnectMonitorDO::getCrmEnterpriseName,request.getCrmEnterpriseName());
        }
        if(null!= request.getSupplierLevel() && request.getSupplierLevel()>0){
            lambdaWrapper.eq(FlowEnterpriseConnectMonitorDO::getSupplierLevel,request.getSupplierLevel());
        }
        if(null!= request.getFlowMode() && request.getFlowMode()>0){
            lambdaWrapper.eq(FlowEnterpriseConnectMonitorDO::getFlowMode,request.getFlowMode());
        }
        if(null!= request.getConnectStatus()){
            lambdaWrapper.eq(FlowEnterpriseConnectMonitorDO::getConnectStatus,request.getConnectStatus());
        }
        if(null !=request.getStartDockingTime() && null!= request.getEndDockingTime()){
            lambdaWrapper.between(FlowEnterpriseConnectMonitorDO::getDockingTime,request.getStartDockingTime(),request.getEndDockingTime());
        }
        if(null !=request.getStartCollectionTime() && null!= request.getEndCollectionTime()){
            lambdaWrapper.between(FlowEnterpriseConnectMonitorDO::getFlowCollectionTime,request.getStartCollectionTime(),request.getEndCollectionTime());
        }
        if(null !=request.getStartFlowDayCount()){
            lambdaWrapper.ge(FlowEnterpriseConnectMonitorDO::getReturnFlowDayCount,request.getStartFlowDayCount());
        }
        if(null!= request.getEndFlowDayCount()){
            lambdaWrapper.le(FlowEnterpriseConnectMonitorDO::getReturnFlowDayCount,request.getEndFlowDayCount());
        }
        //权限相关参数
        if(null != request.getUserDatascopeBO() && OrgDatascopeEnum.PORTION.getCode().equals(request.getUserDatascopeBO().getOrgDatascope())){
            List<Long> crmEids = request.getUserDatascopeBO().getOrgPartDatascopeBO().getCrmEids();
            List<String> provinceCodes = request.getUserDatascopeBO().getOrgPartDatascopeBO().getProvinceCodes();
            if(CollectionUtil.isEmpty(crmEids) && CollectionUtil.isEmpty(provinceCodes)){
                return ListUtil.empty();
            }
            lambdaWrapper.and(wrapper->{
                if(CollectionUtil.isNotEmpty(crmEids)){
                    wrapper.in(FlowEnterpriseConnectMonitorDO::getCrmEnterpriseId,crmEids);
                    if(CollectionUtil.isNotEmpty(provinceCodes)){
                        wrapper.or().in(FlowEnterpriseConnectMonitorDO::getProvinceCode,provinceCodes);
                    }
                }else if(CollectionUtil.isNotEmpty(provinceCodes)){
                    wrapper.in(FlowEnterpriseConnectMonitorDO::getProvinceCode,provinceCodes);
                }
            });
        }
        lambdaWrapper.orderByDesc(FlowEnterpriseConnectMonitorDO::getReturnFlowDayCount);
        return PojoUtils.map(this.list(lambdaWrapper),FlowEnterpriseConnectMonitorDTO.class);
    }

    @Override
    public List<FlowEnterpriseConnectStatisticBO> countConnectionStatusByProvince(Integer supplierLevel) {
        return this.baseMapper.countConnectionStatusByProvince(supplierLevel);
    }

    @Override
    public Integer countMonitorEnterprise(SjmsUserDatascopeBO userDatascopeBO, ConnectStatusEnum connectStatus){
        QueryWrapper<FlowEnterpriseConnectMonitorDO> queryWrapper = new QueryWrapper();
        //权限相关参数
        if(null != userDatascopeBO && OrgDatascopeEnum.PORTION.getCode().equals(userDatascopeBO.getOrgDatascope())){
            List<Long> crmEids = userDatascopeBO.getOrgPartDatascopeBO().getCrmEids();
            List<String> provinceCodes = userDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes();
            if(CollectionUtil.isEmpty(crmEids) && CollectionUtil.isEmpty(provinceCodes)){
                return 0;
            }
            queryWrapper.lambda().and(wrapper->{
                if(CollectionUtil.isNotEmpty(crmEids)){
                    wrapper.in(FlowEnterpriseConnectMonitorDO::getCrmEnterpriseId,crmEids);
                    if(CollectionUtil.isNotEmpty(provinceCodes)){
                        wrapper.or().in(FlowEnterpriseConnectMonitorDO::getProvinceCode,provinceCodes);
                    }
                }else if(CollectionUtil.isNotEmpty(provinceCodes)){
                    wrapper.in(FlowEnterpriseConnectMonitorDO::getProvinceCode,provinceCodes);
                }
            });
        }
        if(null!= connectStatus){
            queryWrapper.lambda().eq(FlowEnterpriseConnectMonitorDO::getConnectStatus,connectStatus.getCode());
        }
        return this.count(queryWrapper);
    }

    @Override
    public Integer deleteByClientId(List<Long> clientIds, String msg, Long opUserId) {
        if(CollectionUtil.isEmpty(clientIds)){
            return 0;
        }
        QueryWrapper<FlowEnterpriseConnectMonitorDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(FlowEnterpriseConnectMonitorDO::getErpClientId,clientIds);
        FlowEnterpriseConnectMonitorDO deleteDO = new FlowEnterpriseConnectMonitorDO();
        deleteDO.setRemark(msg);
        deleteDO.setOpUserId(opUserId);
        return this.batchDeleteWithFill(deleteDO,queryWrapper);
    }

    @Override
    public FlowEnterpriseConnectMonitorDTO findByInstallEmployee(String installEmployee) {
        QueryWrapper<FlowEnterpriseConnectMonitorDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(FlowEnterpriseConnectMonitorDO::getInstallEmployee, installEmployee);
        queryWrapper.lambda().last("limit 1");
        return PojoUtils.map(this.getOne(queryWrapper),FlowEnterpriseConnectMonitorDTO.class);
    }

    @Override
    public List<FlowEnterpriseConnectMonitorDO> findFlowEnterpriseConnectMonitorDTOList() {
        QueryWrapper<FlowEnterpriseConnectMonitorDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(FlowEnterpriseConnectMonitorDO::getDelFlag, 0);
        return this.baseMapper.selectList(queryWrapper);
    }
}
