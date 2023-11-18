package com.yiling.sjms.wash.api;

import com.yiling.sjms.wash.dto.request.UpdateUnlockFlowWashSaleDistributionRequest;

/**
 * @author: shuang.zhang
 * @date: 2023/5/19
 */
public interface SjmsUnlockSaleRuleApi {

    boolean updateUnlockSaleRuleDistribution(UpdateUnlockFlowWashSaleDistributionRequest request);
}
