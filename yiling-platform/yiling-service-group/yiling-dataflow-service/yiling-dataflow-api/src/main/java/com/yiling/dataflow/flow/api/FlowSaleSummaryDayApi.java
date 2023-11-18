package com.yiling.dataflow.flow.api;

import com.yiling.dataflow.flow.dto.request.QueryFlowSaleSummaryRequest;

/**
 * @author: shuang.zhang
 * @date: 2022/11/15
 */
public interface FlowSaleSummaryDayApi {
    /**
     * 通过时间打标流向汇总表
     * @param request
     */
    void updateFlowSaleSummaryDayByDateTimeAndEid(QueryFlowSaleSummaryRequest request);
    /**
     * 最后打自然量和其它商业
     * @param request
     */
    void updateFlowSaleSummaryDayLingShouByTerminalCustomerType(QueryFlowSaleSummaryRequest request);

    /**
     * 最后打自然量和其它商业
     * @param request
     */
    void updateFlowSaleSummaryDayPifaByTerminalCustomerType(QueryFlowSaleSummaryRequest request);
}
