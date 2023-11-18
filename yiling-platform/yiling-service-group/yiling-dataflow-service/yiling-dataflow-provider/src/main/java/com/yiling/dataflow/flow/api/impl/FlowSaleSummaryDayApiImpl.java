package com.yiling.dataflow.flow.api.impl;

import com.yiling.dataflow.flow.api.FlowSaleSummaryDayApi;
import com.yiling.dataflow.flow.dto.request.QueryFlowSaleSummaryRequest;
import com.yiling.dataflow.flow.service.FlowSaleSummaryDayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/11/15
 */
@DubboService
@Slf4j
public class FlowSaleSummaryDayApiImpl implements FlowSaleSummaryDayApi {

    @Autowired
    FlowSaleSummaryDayService flowSaleSummaryDayService;

    @Override
    public void updateFlowSaleSummaryDayByDateTimeAndEid(QueryFlowSaleSummaryRequest request) {
        flowSaleSummaryDayService.updateFlowSaleSummaryDayByDateTimeAndEid(request);
    }

    @Override
    public void updateFlowSaleSummaryDayLingShouByTerminalCustomerType(QueryFlowSaleSummaryRequest request) {
        flowSaleSummaryDayService.updateFlowSaleSummaryDayLingShouByTerminalCustomerType(request);
    }

    @Override
    public void updateFlowSaleSummaryDayPifaByTerminalCustomerType(QueryFlowSaleSummaryRequest request) {
        flowSaleSummaryDayService.updateFlowSaleSummaryDayPifaByTerminalCustomerType(request);
    }
}
