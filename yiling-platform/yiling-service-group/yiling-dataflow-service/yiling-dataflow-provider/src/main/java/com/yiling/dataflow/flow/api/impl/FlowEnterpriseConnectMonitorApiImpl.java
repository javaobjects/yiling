package com.yiling.dataflow.flow.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flow.api.FlowEnterpriseConnectMonitorApi;
import com.yiling.dataflow.flow.bo.FlowEnterpriseConnectStatisticBO;
import com.yiling.dataflow.flow.dto.FlowEnterpriseConnectMonitorDTO;
import com.yiling.dataflow.flow.dto.request.QueryEnterpriseConnectMonitorPageRequest;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseConnectMonitorRequest;
import com.yiling.dataflow.flow.enums.ConnectStatusEnum;
import com.yiling.dataflow.flow.service.FlowEnterpriseConnectMonitorService;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowEnterpriseConnectMonitorApiImpl
 * @描述
 * @创建时间 2023/3/27
 * @修改人 shichen
 * @修改时间 2023/3/27
 **/
@DubboService
@Slf4j
public class FlowEnterpriseConnectMonitorApiImpl implements FlowEnterpriseConnectMonitorApi {

    @Autowired
    private FlowEnterpriseConnectMonitorService flowEnterpriseConnectMonitorService;

    @Override
    public Long saveOrUpdate(SaveFlowEnterpriseConnectMonitorRequest request) {
        return flowEnterpriseConnectMonitorService.saveOrUpdate(request);
    }

    @Override
    public FlowEnterpriseConnectMonitorDTO findByCrmEid(Long crmEnterpriseId) {
        return flowEnterpriseConnectMonitorService.findByCrmEid(crmEnterpriseId);
    }

    @Override
    public Page<FlowEnterpriseConnectMonitorDTO> page(QueryEnterpriseConnectMonitorPageRequest request) {
        return flowEnterpriseConnectMonitorService.page(request);
    }

    @Override
    public List<FlowEnterpriseConnectMonitorDTO> listByQuery(QueryEnterpriseConnectMonitorPageRequest request) {
        return flowEnterpriseConnectMonitorService.listByQuery(request);
    }

    @Override
    public List<FlowEnterpriseConnectStatisticBO> countConnectionStatusByProvince(Integer supplierLevel) {
        return flowEnterpriseConnectMonitorService.countConnectionStatusByProvince(supplierLevel);
    }

    @Override
    public Integer countMonitorEnterprise(SjmsUserDatascopeBO userDatascopeBO, ConnectStatusEnum connectStatus) {
        return flowEnterpriseConnectMonitorService.countMonitorEnterprise(userDatascopeBO,connectStatus);
    }

    @Override
    public Integer deleteByClientId(List<Long> clientIds, String msg, Long opUserId) {
        return flowEnterpriseConnectMonitorService.deleteByClientId(clientIds,msg,opUserId);
    }
}
