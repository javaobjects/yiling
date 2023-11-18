package com.yiling.dataflow.flowcollect.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.api.FlowMonthInventoryApi;
import com.yiling.dataflow.flowcollect.api.FlowMonthSalesApi;
import com.yiling.dataflow.flowcollect.dto.FlowMonthInventoryDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthInventoryRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthSalesRequest;
import com.yiling.dataflow.flowcollect.entity.FlowMonthInventoryDO;
import com.yiling.dataflow.flowcollect.entity.FlowMonthSalesDO;
import com.yiling.dataflow.flowcollect.service.FlowMonthInventoryService;
import com.yiling.dataflow.flowcollect.service.FlowMonthSalesService;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 月流向库存数据 API实现
 *
 * @author: lun.yu
 * @date: 2023-03-06
 */
@Slf4j
@DubboService
public class FlowMonthInventoryApiImpl implements FlowMonthInventoryApi {

    @Autowired
    private FlowMonthInventoryService flowMonthInventoryService;

    @Override
    public boolean updateFlowMonthInventoryAndTask(Long opUserId,Long recordId) {
        return flowMonthInventoryService.updateFlowMonthInventoryAndTask(opUserId,recordId);
    }

    @Override
    public Page<FlowMonthInventoryDTO> queryFlowMonthInventoryPage(QueryFlowMonthPageRequest request) {
        return flowMonthInventoryService.queryFlowMonthInventoryPage(request);
    }

    @Override
    public boolean saveBatch(List<SaveFlowMonthInventoryRequest> request) {
        return flowMonthInventoryService.saveBatch(PojoUtils.map(request,FlowMonthInventoryDO.class));
    }
}
