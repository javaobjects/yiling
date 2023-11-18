package com.yiling.dataflow.flow.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.yiling.dataflow.flow.dao.FlowPurchaseChannelMapper;
import com.yiling.dataflow.flow.dto.FlowPurchaseChannelDTO;
import com.yiling.dataflow.flow.dto.request.QueryFlowPurchaseChannelRequest;
import com.yiling.dataflow.flow.dto.request.SaveFlowPurchaseChannelRequest;
import com.yiling.dataflow.flow.entity.FlowPurchaseChannelDO;
import com.yiling.dataflow.flow.service.FlowPurchaseChannelService;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;


/**
 * @author shichen
 * @类名 FlowPurchaseChannelServiceImpl
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Service
public class FlowPurchaseChannelServiceImpl extends BaseServiceImpl<FlowPurchaseChannelMapper, FlowPurchaseChannelDO> implements FlowPurchaseChannelService {

    @Override
    public Page<FlowPurchaseChannelDTO> pageList(QueryFlowPurchaseChannelRequest request) {
        QueryWrapper<FlowPurchaseChannelDO> queryWrapper = new QueryWrapper();
        if(null != request.getCrmOrgId()){
            queryWrapper.lambda().eq(FlowPurchaseChannelDO::getCrmOrgId,request.getCrmOrgId());
        }else {
            if(StringUtils.isNotBlank(request.getOrgName())){
                queryWrapper.lambda().like(FlowPurchaseChannelDO::getOrgName,request.getOrgName());
            }
        }
        if(null != request.getCrmPurchaseOrgId()){
            queryWrapper.lambda().eq(FlowPurchaseChannelDO::getCrmPurchaseOrgId,request.getCrmPurchaseOrgId());
        }else {
            if(StringUtils.isNotBlank(request.getPurchaseOrgName())){
                queryWrapper.lambda().like(FlowPurchaseChannelDO::getPurchaseOrgName,request.getPurchaseOrgName());
            }
        }
        if(StringUtils.isNotBlank(request.getProvinceCode())){
            queryWrapper.lambda().eq(FlowPurchaseChannelDO::getProvinceCode,request.getProvinceCode());
        }
        if(StringUtils.isNotBlank(request.getCityCode())){
            queryWrapper.lambda().eq(FlowPurchaseChannelDO::getCityCode,request.getCityCode());
        }
        if(StringUtils.isNotBlank(request.getRegionCode())){
            queryWrapper.lambda().eq(FlowPurchaseChannelDO::getRegionCode,request.getRegionCode());
        }
        //权限相关参数
        if(null != request.getUserDatascopeBO() && OrgDatascopeEnum.PORTION.getCode().equals(request.getUserDatascopeBO().getOrgDatascope())){
            List<Long> crmEids = request.getUserDatascopeBO().getOrgPartDatascopeBO().getCrmEids();
            List<String> provinceCodes = request.getUserDatascopeBO().getOrgPartDatascopeBO().getProvinceCodes();
            if(CollectionUtil.isEmpty(crmEids) && CollectionUtil.isEmpty(provinceCodes)){
                return new Page<>(request.getCurrent(),request.getSize());
            }
            queryWrapper.lambda().and(wrapper->{
                if(CollectionUtil.isNotEmpty(crmEids)){
                    wrapper.in(FlowPurchaseChannelDO::getCrmOrgId,crmEids);
                    if(CollectionUtil.isNotEmpty(provinceCodes)){
                        wrapper.or().in(FlowPurchaseChannelDO::getProvinceCode,provinceCodes);
                    }
                }else if(CollectionUtil.isNotEmpty(provinceCodes)){
                    wrapper.in(FlowPurchaseChannelDO::getProvinceCode,provinceCodes);
                }
            });
        }
        queryWrapper.lambda().orderByDesc(FlowPurchaseChannelDO::getUpdateTime);
        return PojoUtils.map(this.page(request.getPage(),queryWrapper),FlowPurchaseChannelDTO.class);
    }

    @Override
    public Long save(SaveFlowPurchaseChannelRequest request) {
        FlowPurchaseChannelDTO purchaseChannelDTO = this.findByOrgIdAndPurchaseOrgId(request.getCrmOrgId(), request.getCrmPurchaseOrgId());
        if(null!=request.getId() && request.getId()>0){
            if(null!=purchaseChannelDTO && !request.getId().equals(purchaseChannelDTO.getId())){
                throw new BusinessException(ResultCode.FAILED,"机构编码和采购渠道机构对应的采购渠道已存在");
            }
            FlowPurchaseChannelDO channelDO = PojoUtils.map(request, FlowPurchaseChannelDO.class);
            this.updateById(channelDO);
            return channelDO.getId();
        }

        if(null!=purchaseChannelDTO){
            throw new BusinessException(ResultCode.FAILED,"机构编码和采购渠道机构对应的采购渠道已存在");
        }
        FlowPurchaseChannelDO purchaseChannelDO = PojoUtils.map(request, FlowPurchaseChannelDO.class);
        this.save(purchaseChannelDO);
        return purchaseChannelDO.getId();
    }

    @Override
    public FlowPurchaseChannelDTO findByOrgIdAndPurchaseOrgId(Long orgId, Long purchaseOrgId) {
        if(null==orgId || null == purchaseOrgId){
            throw new BusinessException(ResultCode.FAILED,"机构编码或者采购渠道机构编码为空");
        }
        QueryWrapper<FlowPurchaseChannelDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(FlowPurchaseChannelDO::getCrmOrgId,orgId)
                .eq(FlowPurchaseChannelDO::getCrmPurchaseOrgId,purchaseOrgId)
                .last("limit 1");
        return PojoUtils.map(this.getOne(queryWrapper),FlowPurchaseChannelDTO.class);
    }

    @Override
    public List<FlowPurchaseChannelDTO> findByPurchaseOrgId(Long purchaseOrgId) {
        Assert.notNull(purchaseOrgId,"采购渠道机构编码不能为空");
        QueryWrapper<FlowPurchaseChannelDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(FlowPurchaseChannelDO::getCrmPurchaseOrgId,purchaseOrgId);
        return PojoUtils.map(this.list(queryWrapper),FlowPurchaseChannelDTO.class);
    }
}
