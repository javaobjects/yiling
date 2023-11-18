package com.yiling.dataflow.flow.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flow.api.FlowEnterpriseSupplierMappingApi;
import com.yiling.dataflow.flow.dto.FlowEnterpriseSupplierMappingDTO;
import com.yiling.dataflow.flow.dto.request.QueryFlowEnterpriseSupplierMappingPageRequest;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseSupplierMappingRequest;
import com.yiling.dataflow.flow.entity.FlowEnterpriseSupplierMappingDO;
import com.yiling.dataflow.flow.service.FlowEnterpriseSupplierMappingService;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowEnterpriseSupplierMappingApiImpl
 * @描述
 * @创建时间 2023/5/31
 * @修改人 shichen
 * @修改时间 2023/5/31
 **/
@DubboService
@Slf4j
public class FlowEnterpriseSupplierMappingApiImpl implements FlowEnterpriseSupplierMappingApi {
    @Autowired
    private FlowEnterpriseSupplierMappingService flowEnterpriseSupplierMappingService;

    @Override
    public Long save(SaveFlowEnterpriseSupplierMappingRequest request) {
        return flowEnterpriseSupplierMappingService.saveOrUpdate(request);
    }

    @Override
    public Boolean deleteById(Long id, Long opUserId) {
        FlowEnterpriseSupplierMappingDO deleteDO = new FlowEnterpriseSupplierMappingDO();
        deleteDO.setId(id);
        deleteDO.setOpUserId(opUserId);
        deleteDO.setOpTime(new Date());
        return flowEnterpriseSupplierMappingService.deleteByIdWithFill(deleteDO)>0;
    }

    @Override
    public FlowEnterpriseSupplierMappingDTO findById(Long id) {
        return PojoUtils.map(flowEnterpriseSupplierMappingService.getById(id),FlowEnterpriseSupplierMappingDTO.class);
    }

    @Override
    public List<FlowEnterpriseSupplierMappingDTO> findByIds(List<Long> ids) {
        return PojoUtils.map(flowEnterpriseSupplierMappingService.listByIds(ids),FlowEnterpriseSupplierMappingDTO.class);
    }

    @Override
    public Page<FlowEnterpriseSupplierMappingDTO> pageList(QueryFlowEnterpriseSupplierMappingPageRequest request) {
        return flowEnterpriseSupplierMappingService.pageList(request);
    }

    @Override
    public Long saveOrUpdate(SaveFlowEnterpriseSupplierMappingRequest request) {
        return flowEnterpriseSupplierMappingService.saveOrUpdate(request);
    }

    @Override
    public FlowEnterpriseSupplierMappingDTO findBySupplierNameAndCrmEnterpriseId(String flowSupplierName, Long crmEnterpriseId) {
        return flowEnterpriseSupplierMappingService.findBySupplierNameAndCrmEnterpriseId(flowSupplierName,crmEnterpriseId);
    }

    @Override
    public Boolean batchUpdateById(List<SaveFlowEnterpriseSupplierMappingRequest> requestList) {
        return flowEnterpriseSupplierMappingService.batchUpdateById(requestList);
    }

    @Override
    public Boolean sendRefreshSupplierFlowMq(List<FlowEnterpriseSupplierMappingDTO> mappingList) {
        return flowEnterpriseSupplierMappingService.sendRefreshSupplierFlowMq(mappingList);
    }
}
