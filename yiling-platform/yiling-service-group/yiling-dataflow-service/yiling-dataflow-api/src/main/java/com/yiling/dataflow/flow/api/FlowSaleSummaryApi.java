package com.yiling.dataflow.flow.api;

import com.yiling.dataflow.flow.dto.request.UpdateFlowSaleSummaryRequest;

/**
 * @author: shuang.zhang
 * @date: 2022/11/15
 */
public interface FlowSaleSummaryApi {
    /**
     * 通过时间或者eid更新汇总表信息
     * @param request
     */
    void updateFlowSaleSummaryByDateTimeAndEid(UpdateFlowSaleSummaryRequest request);
}
