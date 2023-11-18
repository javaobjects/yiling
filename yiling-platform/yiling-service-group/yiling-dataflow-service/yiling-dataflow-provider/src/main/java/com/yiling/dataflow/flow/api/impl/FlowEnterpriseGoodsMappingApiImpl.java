package com.yiling.dataflow.flow.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.flow.api.FlowEnterpriseGoodsMappingApi;
import com.yiling.dataflow.flow.dto.FlowEnterpriseGoodsMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseGoodsMappingRequest;
import com.yiling.dataflow.flow.entity.FlowEnterpriseGoodsMappingDO;
import com.yiling.dataflow.flow.service.FlowEnterpriseGoodsMappingService;
import com.yiling.framework.common.util.PojoUtils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowEnterpriseGoodsMappingApiImpl
 * @描述
 * @创建时间 2023/2/28
 * @修改人 shichen
 * @修改时间 2023/2/28
 **/
@DubboService
@Slf4j
public class FlowEnterpriseGoodsMappingApiImpl implements FlowEnterpriseGoodsMappingApi {
    @Autowired
    private FlowEnterpriseGoodsMappingService flowEnterpriseGoodsMappingService;

    @Override
    public Long save(SaveFlowEnterpriseGoodsMappingRequest request) {
        return flowEnterpriseGoodsMappingService.saveOrUpdate(request);
    }

    @Override
    public Boolean deleteById(Long id, Long opUserId) {
        FlowEnterpriseGoodsMappingDO deleteDO = new FlowEnterpriseGoodsMappingDO();
        deleteDO.setId(id);
        deleteDO.setOpUserId(opUserId);
        deleteDO.setOpTime(new Date());
        return flowEnterpriseGoodsMappingService.deleteByIdWithFill(deleteDO)>0;
    }

    @Override
    public FlowEnterpriseGoodsMappingDTO findById(Long id) {
        return PojoUtils.map(flowEnterpriseGoodsMappingService.getById(id),FlowEnterpriseGoodsMappingDTO.class);
    }

    @Override
    public List<FlowEnterpriseGoodsMappingDTO> findByIds(List<Long> ids) {
        if(CollectionUtil.isEmpty(ids)){
            return ListUtil.empty();
        }
        return PojoUtils.map(flowEnterpriseGoodsMappingService.listByIds(ids),FlowEnterpriseGoodsMappingDTO.class);
    }

    @Override
    public FlowEnterpriseGoodsMappingDTO findByFlowGoodsNameAndFlowSpecificationAndCrmEnterpriseId(String flowGoodsName, String flowSpecification, Long crmEnterpriseId) {
        return flowEnterpriseGoodsMappingService.findByFlowGoodsNameAndFlowSpecificationAndCrmEnterpriseId(flowGoodsName,flowSpecification,crmEnterpriseId);
    }

    @Override
    public Boolean batchUpdateById(List<SaveFlowEnterpriseGoodsMappingRequest> requestList) {
        return flowEnterpriseGoodsMappingService.batchUpdateById(requestList);
    }

    @Override
    public Boolean sendRefreshGoodsFlowMq(List<FlowEnterpriseGoodsMappingDTO> mappingList) {
        return flowEnterpriseGoodsMappingService.sendRefreshGoodsFlowMq(mappingList);
    }

    @Override
    public FlowEnterpriseGoodsMappingDTO findByCrmEnterpriseIdAndCrmGoodsCode(Long crmEnterpriseId, Long crmGoodsCode) {
        return flowEnterpriseGoodsMappingService.findByCrmEnterpriseIdAndCrmGoodsCode(crmEnterpriseId, crmGoodsCode);
    }
}
