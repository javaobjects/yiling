package com.yiling.dataflow;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.dataflow.order.api.FlowGoodsBatchApi;
import com.yiling.dataflow.order.api.FlowPurchaseApi;
import com.yiling.dataflow.order.api.FlowSaleApi;

/**
 * @author fucheng.bai
 * @date 2022/7/25
 */
public class FlowSpecSyncTest extends BaseTest {

    @Autowired
    private FlowPurchaseApi flowPurchaseApi;

    @Autowired
    private FlowSaleApi flowSaleApi;

    @Autowired
    private FlowGoodsBatchApi flowGoodsBatchApi;

    @Test
    public void syncFlowSpecTest() {
        flowPurchaseApi.syncFlowPurchaseSpec();
//        flowSaleApi.syncFlowSaleSpec();
//        flowGoodsBatchApi.syncFlowGoodsBatchSpec();
    }
}
