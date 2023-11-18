package com.yiling.dataflow.flow.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.flow.api.FlowEnterpriseCustomerMappingApi;
import com.yiling.dataflow.flow.dto.FlowEnterpriseCustomerMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseCustomerMappingRequest;
import com.yiling.dataflow.flow.entity.FlowEnterpriseCustomerMappingDO;
import com.yiling.dataflow.flow.service.FlowEnterpriseCustomerMappingService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowEnterpriseCustomerMappingApiImpl
 * @描述
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@DubboService
@Slf4j
public class FlowEnterpriseCustomerMappingApiImpl implements FlowEnterpriseCustomerMappingApi {
    @Autowired
    private FlowEnterpriseCustomerMappingService flowEnterpriseCustomerMappingService;

    @Override
    public Long save(SaveFlowEnterpriseCustomerMappingRequest request) {
        return flowEnterpriseCustomerMappingService.saveOrUpdate(request);
    }

    @Override
    public Boolean deleteById(Long id,Long opUserId) {
        FlowEnterpriseCustomerMappingDO deleteDO = new FlowEnterpriseCustomerMappingDO();
        deleteDO.setId(id);
        deleteDO.setOpUserId(opUserId);
        deleteDO.setOpTime(new Date());
        return flowEnterpriseCustomerMappingService.deleteByIdWithFill(deleteDO)>0;
    }

    @Override
    public FlowEnterpriseCustomerMappingDTO findById(Long id) {
        return PojoUtils.map(flowEnterpriseCustomerMappingService.getById(id),FlowEnterpriseCustomerMappingDTO.class);
    }

    @Override
    public List<FlowEnterpriseCustomerMappingDTO> findByIds(List<Long> ids) {
        if(CollectionUtil.isEmpty(ids)){
            return ListUtil.empty();
        }
        return PojoUtils.map(flowEnterpriseCustomerMappingService.listByIds(ids),FlowEnterpriseCustomerMappingDTO.class);
    }

    @Override
    public FlowEnterpriseCustomerMappingDTO findByCustomerNameAndCrmEnterpriseId(String flowCustomerName, Long crmEnterpriseId) {
        return flowEnterpriseCustomerMappingService.findByCustomerNameAndCrmEnterpriseId(flowCustomerName,crmEnterpriseId);
    }

    @Override
    public Boolean batchUpdateById(List<SaveFlowEnterpriseCustomerMappingRequest> requestList) {
        return flowEnterpriseCustomerMappingService.batchUpdateById(requestList);
    }

    @Override
    public Boolean sendRefreshCustomerFlowMq(List<FlowEnterpriseCustomerMappingDTO> mappingList) {
        return flowEnterpriseCustomerMappingService.sendRefreshCustomerFlowMq(mappingList);
    }

    @Override
    public List<FlowEnterpriseCustomerMappingDTO> findUnmappedByFlowCustomerName(String flowCustomerName) {
        return flowEnterpriseCustomerMappingService.findUnmappedByFlowCustomerName(flowCustomerName);
    }
}
