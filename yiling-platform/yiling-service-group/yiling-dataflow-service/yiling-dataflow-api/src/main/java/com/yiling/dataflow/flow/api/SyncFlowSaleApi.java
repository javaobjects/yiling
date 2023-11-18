package com.yiling.dataflow.flow.api;

import java.util.List;

/**
 * @author: shuang.zhang
 * @date: 2022/12/21
 */
public interface SyncFlowSaleApi {
    void insertList(List<Long> ids);
    void syncFlowSaleSummary();
}
