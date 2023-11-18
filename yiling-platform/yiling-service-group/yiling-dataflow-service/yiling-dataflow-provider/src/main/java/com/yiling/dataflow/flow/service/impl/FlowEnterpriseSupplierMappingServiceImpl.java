package com.yiling.dataflow.flow.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.mq.api.MqMessageSendApi;
import com.yiling.basic.mq.bo.MqMessageBO;
import com.yiling.dataflow.flow.dao.FlowEnterpriseSupplierMappingMapper;
import com.yiling.dataflow.flow.dto.FlowEnterpriseSupplierMappingDTO;
import com.yiling.dataflow.flow.dto.request.QueryFlowEnterpriseSupplierMappingPageRequest;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseSupplierMappingRequest;
import com.yiling.dataflow.flow.entity.FlowEnterpriseSupplierMappingDO;
import com.yiling.dataflow.flow.service.FlowEnterpriseSupplierMappingService;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowEnterpriseSupplierMappingServiceImpl
 * @描述
 * @创建时间 2023/5/31
 * @修改人 shichen
 * @修改时间 2023/5/31
 **/
@Service
@Slf4j
public class FlowEnterpriseSupplierMappingServiceImpl extends BaseServiceImpl<FlowEnterpriseSupplierMappingMapper, FlowEnterpriseSupplierMappingDO> implements FlowEnterpriseSupplierMappingService {

    @Autowired
    @Lazy
    FlowEnterpriseSupplierMappingServiceImpl _this;
    @DubboReference
    MqMessageSendApi mqMessageSendApi;

    @Override
    public Page<FlowEnterpriseSupplierMappingDTO> pageList(QueryFlowEnterpriseSupplierMappingPageRequest request) {
        QueryWrapper<FlowEnterpriseSupplierMappingDO> queryWrapper = new QueryWrapper();
        if(StringUtils.isNotBlank(request.getFlowSupplierName())){
            queryWrapper.lambda().like(FlowEnterpriseSupplierMappingDO::getFlowSupplierName,request.getFlowSupplierName());
        }
        if(null != request.getCrmOrgId()){
            queryWrapper.lambda().eq(FlowEnterpriseSupplierMappingDO::getCrmOrgId,request.getCrmOrgId());
        } else {
            queryWrapper.lambda().gt(FlowEnterpriseSupplierMappingDO::getCrmOrgId,0);
            if(StringUtils.isNotBlank(request.getOrgName())){
                queryWrapper.lambda().like(FlowEnterpriseSupplierMappingDO::getOrgName,request.getOrgName());
            }
        }

        if(null != request.getCrmEnterpriseId()){
            queryWrapper.lambda().eq(FlowEnterpriseSupplierMappingDO::getCrmEnterpriseId,request.getCrmEnterpriseId());
        } else {
            if(StringUtils.isNotBlank(request.getEnterpriseName())){
                queryWrapper.lambda().like(FlowEnterpriseSupplierMappingDO::getEnterpriseName,request.getEnterpriseName());

            }
        }
        if(StringUtils.isNotBlank(request.getProvinceCode())){
            queryWrapper.lambda().eq(FlowEnterpriseSupplierMappingDO::getProvinceCode,request.getProvinceCode());
        }

        if(null != request.getStartUpdateTime() && null != request.getEndUpdateTime()){
            queryWrapper.lambda().between(FlowEnterpriseSupplierMappingDO::getUpdateTime,request.getStartUpdateTime(),request.getEndUpdateTime());
        }
        if(null != request.getStartLastUploadTime() && null != request.getEndLastUploadTime()){
            queryWrapper.lambda().between(FlowEnterpriseSupplierMappingDO::getLastUploadTime,request.getStartLastUploadTime(),request.getEndLastUploadTime());
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
                    wrapper.in(FlowEnterpriseSupplierMappingDO::getCrmOrgId,crmEids);
                    if(CollectionUtil.isNotEmpty(provinceCodes)){
                        wrapper.or().in(FlowEnterpriseSupplierMappingDO::getProvinceCode,provinceCodes);
                    }
                }else if(CollectionUtil.isNotEmpty(provinceCodes)){
                    wrapper.in(FlowEnterpriseSupplierMappingDO::getProvinceCode,provinceCodes);
                }
            });
        }
        if(null != request.getOrderByUploadTime() && request.getOrderByUploadTime()){
            queryWrapper.lambda().orderByDesc(FlowEnterpriseSupplierMappingDO::getLastUploadTime);
        }else {
            queryWrapper.lambda().orderByDesc(FlowEnterpriseSupplierMappingDO::getUpdateTime);
        }
        return PojoUtils.map(this.page(request.getPage(),queryWrapper),FlowEnterpriseSupplierMappingDTO.class);
    }

    @Override
    public Long saveOrUpdate(SaveFlowEnterpriseSupplierMappingRequest request) {
        if(request.getId()!=null && request.getId()>0){
            this.updateById(PojoUtils.map(request, FlowEnterpriseSupplierMappingDO.class));
            return request.getId();
        }else {
            FlowEnterpriseSupplierMappingDTO mappingDTO = this.findBySupplierNameAndCrmEnterpriseId(request.getFlowSupplierName(), request.getCrmEnterpriseId());
            if(null!=mappingDTO){
                request.setId(mappingDTO.getId());
                this.updateById(PojoUtils.map(request,FlowEnterpriseSupplierMappingDO.class));
                return request.getId();
            }
            FlowEnterpriseSupplierMappingDO mappingDO = PojoUtils.map(request, FlowEnterpriseSupplierMappingDO.class);
            this.save(mappingDO);
            return mappingDO.getId();
        }
    }

    @Override
    public FlowEnterpriseSupplierMappingDTO findBySupplierNameAndCrmEnterpriseId(String flowSupplierName, Long crmEnterpriseId) {
        QueryWrapper<FlowEnterpriseSupplierMappingDO> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(FlowEnterpriseSupplierMappingDO::getFlowSupplierName,flowSupplierName)
                .eq(FlowEnterpriseSupplierMappingDO::getCrmEnterpriseId,crmEnterpriseId)
                .last("limit 1");
        return PojoUtils.map(this.getOne(queryWrapper),FlowEnterpriseSupplierMappingDTO.class);
    }

    @Override
    public Boolean batchUpdateById(List<SaveFlowEnterpriseSupplierMappingRequest> requestList) {
        List<SaveFlowEnterpriseSupplierMappingRequest> filterList = requestList.stream().filter(request -> null == request.getId() || request.getId() <= 0).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(filterList)){
            throw new BusinessException(ResultCode.FAILED,"更新数据的id为空");
        }
        return this.updateBatchById(PojoUtils.map(requestList, FlowEnterpriseSupplierMappingDO.class));
    }

    @Override
    public Boolean sendRefreshSupplierFlowMq(List<FlowEnterpriseSupplierMappingDTO> mappingList) {
        log.info("供应商映射刷新流向参数：{}",mappingList);
        return _this.sendMq(Constants.TOPIC_SUPPLIER_MAPPING_MONTH_WASH,Constants.TAG_SUPPLIER_MAPPING_MONTH_WASH, JSON.toJSONString(mappingList));
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public boolean sendMq(String topic,String topicTag,String msg) {
        MqMessageBO mqMessageBO = _this.sendPrepare(topic, topicTag, msg);
        mqMessageSendApi.send(mqMessageBO);
        return true;
    }

    /**
     * 消息持久化
     *
     * @param topic
     * @param topicTag
     * @param msg
     * @return
     */
    public MqMessageBO sendPrepare(String topic,String topicTag,String msg) {
        MqMessageBO mqMessageBO = new MqMessageBO(topic, topicTag, msg);
        mqMessageBO = mqMessageSendApi.prepare(mqMessageBO);
        return mqMessageBO;
    }
}
