package com.yiling.open.erp.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.open.erp.api.ErpGoodsBatchFlowApi;
import com.yiling.open.erp.service.ErpGoodsBatchFlowService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/2/22
 */
@DubboService
@Slf4j
public class ErpGoodsBatchFlowApiImpl implements ErpGoodsBatchFlowApi {

    @Autowired
    private ErpGoodsBatchFlowService erpGoodsBatchFlowService;

    @Override
    public void syncGoodsBatchFlow() {
        try {
            erpGoodsBatchFlowService.syncGoodsBatchFlow();
        } catch (Exception e) {
            log.error("[ErpGoodsBatchFlowApiImpl][syncGoodsBatchFlow] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public Integer updateOperTypeGoodsBatchFlowBySuId(Long suId) {
        return erpGoodsBatchFlowService.updateOperTypeGoodsBatchFlowBySuId(suId);
    }
}
