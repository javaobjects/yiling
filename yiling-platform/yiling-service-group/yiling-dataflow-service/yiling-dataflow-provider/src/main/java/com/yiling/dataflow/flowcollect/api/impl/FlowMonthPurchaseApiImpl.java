package com.yiling.dataflow.flowcollect.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.flowcollect.api.FlowMonthInventoryApi;
import com.yiling.dataflow.flowcollect.api.FlowMonthPurchaseApi;
import com.yiling.dataflow.flowcollect.dto.FlowMonthPurchaseDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthInventoryRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthPurchaseRequest;
import com.yiling.dataflow.flowcollect.entity.FlowMonthInventoryDO;
import com.yiling.dataflow.flowcollect.entity.FlowMonthPurchaseDO;
import com.yiling.dataflow.flowcollect.service.FlowMonthInventoryService;
import com.yiling.dataflow.flowcollect.service.FlowMonthPurchaseService;
import com.yiling.framework.common.util.PojoUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 月流向采购数据 API实现
 *
 * @author: lun.yu
 * @date: 2023-03-06
 */
@Slf4j
@DubboService
public class FlowMonthPurchaseApiImpl implements FlowMonthPurchaseApi {

    @Autowired
    private FlowMonthPurchaseService flowMonthPurchaseService;

    @Override
    public boolean updateFlowMonthPurchaseAndTask(Long opUserId,Long recordId) {
        return flowMonthPurchaseService.updateFlowMonthPurchaseAndTask(opUserId,recordId);
    }

    @Override
    public Page<FlowMonthPurchaseDTO> queryFlowMonthPurchasePage(QueryFlowMonthPageRequest request) {
        return flowMonthPurchaseService.queryFlowMonthPurchasePage(request);
    }

    @Override
    public boolean saveBatch(List<SaveFlowMonthPurchaseRequest> request) {
        return flowMonthPurchaseService.saveBatch(PojoUtils.map(request, FlowMonthPurchaseDO.class));
    }
}
