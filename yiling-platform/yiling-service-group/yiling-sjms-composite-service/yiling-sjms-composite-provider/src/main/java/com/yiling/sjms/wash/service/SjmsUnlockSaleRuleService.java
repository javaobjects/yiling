package com.yiling.sjms.wash.service;

import com.yiling.dataflow.wash.dto.UnlockFlowWashSaleDTO;
import com.yiling.dataflow.wash.dto.UnlockSaleRuleDTO;
import com.yiling.sjms.wash.dto.request.UpdateUnlockFlowWashSaleDistributionRequest;

/**
 * @author: shuang.zhang
 * @date: 2023/5/19
 */
public interface SjmsUnlockSaleRuleService {

    boolean updateUnlockSaleRuleDistribution(UpdateUnlockFlowWashSaleDistributionRequest request);
}
