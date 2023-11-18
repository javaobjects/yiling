package com.yiling.open.erp.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.open.erp.api.ErpPurchaseFlowApi;
import com.yiling.open.erp.service.ErpPurchaseFlowService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/2/22
 */
@DubboService
@Slf4j
public class ErpPurchaseFlowApiImpl implements ErpPurchaseFlowApi {

    @Autowired
    private ErpPurchaseFlowService erpPurchaseFlowService;

    @Override
    public void syncPurchaseFlow() {
        try {
            erpPurchaseFlowService.synPurchaseFlow();
        } catch (Exception e) {
            log.error("[ErpPurchaseFlowApiImpl][syncPurchaseFlow] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

}
