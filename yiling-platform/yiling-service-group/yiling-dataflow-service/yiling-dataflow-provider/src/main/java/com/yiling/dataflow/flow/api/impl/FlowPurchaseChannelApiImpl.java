package com.yiling.dataflow.flow.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flow.api.FlowPurchaseChannelApi;
import com.yiling.dataflow.flow.dto.FlowPurchaseChannelDTO;
import com.yiling.dataflow.flow.dto.request.QueryFlowPurchaseChannelRequest;
import com.yiling.dataflow.flow.dto.request.SaveFlowPurchaseChannelRequest;
import com.yiling.dataflow.flow.entity.FlowPurchaseChannelDO;
import com.yiling.dataflow.flow.service.FlowPurchaseChannelService;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 FlowPurchaseChannelApiImpl
 * @描述
 * @创建时间 2023/3/2
 * @修改人 shichen
 * @修改时间 2023/3/2
 **/
@DubboService
@Slf4j
public class FlowPurchaseChannelApiImpl implements FlowPurchaseChannelApi {

    @Autowired
    private FlowPurchaseChannelService flowPurchaseChannelService;


    @Override
    public Page<FlowPurchaseChannelDTO> pageList(QueryFlowPurchaseChannelRequest request) {
        return flowPurchaseChannelService.pageList(request);
    }

    @Override
    public Long save(SaveFlowPurchaseChannelRequest request) {
        return flowPurchaseChannelService.save(request);
    }

    @Override
    public FlowPurchaseChannelDTO findByOrgIdAndPurchaseOrgId(Long orgId, Long purchaseOrgId) {
        return flowPurchaseChannelService.findByOrgIdAndPurchaseOrgId(orgId,purchaseOrgId);
    }

    @Override
    public Boolean deleteById(Long id, Long opUserId) {
        FlowPurchaseChannelDO deleteDO = new FlowPurchaseChannelDO();
        deleteDO.setId(id);
        deleteDO.setOpUserId(opUserId);
        deleteDO.setOpTime(new Date());
        return flowPurchaseChannelService.deleteByIdWithFill(deleteDO)>0;
    }

    @Override
    public FlowPurchaseChannelDTO findById(Long id) {
        return PojoUtils.map(flowPurchaseChannelService.getById(id),FlowPurchaseChannelDTO.class);
    }

    @Override
    public List<FlowPurchaseChannelDTO> findByPurchaseOrgId(Long purchaseOrgId) {
        return flowPurchaseChannelService.findByPurchaseOrgId(purchaseOrgId);
    }
}
