package com.yiling.dataflow.flow.api.impl;

import com.yiling.dataflow.flow.dto.request.UpdateFlowPurchaseSalesInventoryRequest;
import com.yiling.dataflow.flow.service.FlowPurchaseSalesInventoryService;
import com.yiling.dataflow.flow.api.FlowPurchaseSalesInventoryApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/11/15
 */
@DubboService
@Slf4j
public class FlowPurchaseSalesInventoryApiImpl implements FlowPurchaseSalesInventoryApi {

    @Autowired
    private FlowPurchaseSalesInventoryService flowPurchaseSalesInventoryService;

    @Override
    public void updateFlowPurchaseSalesInventoryByJob(UpdateFlowPurchaseSalesInventoryRequest request) {
        flowPurchaseSalesInventoryService.updateFlowPurchaseSalesInventoryByJob(request);
    }
}
