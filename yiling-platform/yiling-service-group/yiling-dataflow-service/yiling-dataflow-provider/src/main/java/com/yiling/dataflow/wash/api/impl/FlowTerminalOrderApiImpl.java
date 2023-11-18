package com.yiling.dataflow.wash.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.wash.api.FlowTerminalOrderApi;
import com.yiling.dataflow.wash.dto.FlowGoodsBatchTransitDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchTransitPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveFlowGoodsBatchTransitRequest;
import com.yiling.dataflow.wash.dto.request.UpdateFlowGoodsBatchTransitRequest;
import com.yiling.dataflow.wash.service.FlowGoodsBatchTransitService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author: houjie.sun
 * @date: 2023/3/6
 */
@DubboService
public class FlowTerminalOrderApiImpl implements FlowTerminalOrderApi {

    @Autowired
    private FlowGoodsBatchTransitService flowGoodsBatchTransitService;


    @Override
    public Page<FlowGoodsBatchTransitDTO> listPage(QueryFlowGoodsBatchTransitPageRequest request) {
        return PojoUtils.map(flowGoodsBatchTransitService.listPage(request), FlowGoodsBatchTransitDTO.class);
    }

    @Override
    public List<FlowGoodsBatchTransitDTO> listByEnterpriseAndSupplyIdAndGoodsCode(Integer goodsBatchType, List<Long> crmEnterpriseIdList, List<Long> crmGoodsCodeList) {
        return PojoUtils.map(flowGoodsBatchTransitService.listByEnterpriseAndSupplyIdAndGoodsCode(goodsBatchType, crmEnterpriseIdList, null, crmGoodsCodeList), FlowGoodsBatchTransitDTO.class);
    }

    @Override
    public Boolean batchSave(List<SaveFlowGoodsBatchTransitRequest> list) {
        return flowGoodsBatchTransitService.batchSave(list);
    }

    @Override
    public FlowGoodsBatchTransitDTO getById(Long id) {
        return PojoUtils.map(flowGoodsBatchTransitService.getById(id), FlowGoodsBatchTransitDTO.class);
    }

    @Override
    public Boolean batchUpdate(List<UpdateFlowGoodsBatchTransitRequest> list) {
        return flowGoodsBatchTransitService.batchUpdate(list);
    }

    @Override
    public int deleteById(Long id, Long currentUserId) {
        return flowGoodsBatchTransitService.deleteById(id, currentUserId);
    }
}
