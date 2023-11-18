package com.yiling.dataflow.flow.api;

import com.yiling.dataflow.flow.dto.request.UpdateFlowPurchaseSalesInventoryRequest;

/**
 * @author: shuang.zhang
 * @date: 2022/11/15
 */
public interface FlowPurchaseSalesInventoryApi {
    /**
     * 每天统计进销存汇总（从1号到现在时间）
     * @param request
     */
    void updateFlowPurchaseSalesInventoryByJob(UpdateFlowPurchaseSalesInventoryRequest request);
}
