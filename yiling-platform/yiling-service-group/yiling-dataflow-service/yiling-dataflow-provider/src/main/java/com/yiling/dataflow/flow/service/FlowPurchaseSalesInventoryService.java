package com.yiling.dataflow.flow.service;

import java.util.List;

import com.yiling.dataflow.flow.dto.request.UpdateFlowPurchaseSalesInventoryRequest;
import com.yiling.dataflow.flow.entity.FlowPurchaseSalesInventoryDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-11-14
 */
public interface FlowPurchaseSalesInventoryService extends BaseService<FlowPurchaseSalesInventoryDO> {
    /**
     * 通过flowSaleIds更新汇总表信息
     */
    void updateFlowPurchaseSalesInventoryByJob(UpdateFlowPurchaseSalesInventoryRequest request);
}
