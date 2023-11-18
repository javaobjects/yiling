package com.yiling.sjms.wash.api.impl;

import com.yiling.sjms.wash.api.SjmsUnlockSaleRuleApi;
import com.yiling.sjms.wash.dto.request.UpdateUnlockFlowWashSaleDistributionRequest;
import com.yiling.sjms.wash.service.SjmsUnlockSaleRuleService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2023/5/19
 */
@DubboService
public class SjmsUnlockSaleRuleApiImpl implements SjmsUnlockSaleRuleApi {

    @Autowired
    private SjmsUnlockSaleRuleService sjmsUnlockSaleRuleService;

    @Override
    public boolean updateUnlockSaleRuleDistribution(UpdateUnlockFlowWashSaleDistributionRequest request) {
        return sjmsUnlockSaleRuleService.updateUnlockSaleRuleDistribution(request);
    }
}
