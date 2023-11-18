package com.yiling.open.erp.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.open.erp.api.ErpSaleFlowApi;
import com.yiling.open.erp.service.ErpPurchaseFlowService;
import com.yiling.open.erp.service.ErpSaleFlowService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/2/22
 */
@DubboService
@Slf4j
public class ErpSaleFlowApiImpl implements ErpSaleFlowApi {

    @Autowired
    private ErpSaleFlowService erpSaleFlowService;

    @Override
    public void syncSaleFlow() {
        try {
            erpSaleFlowService.synSaleFlow();
        } catch (Exception e) {
            log.error("[ErpSaleFlowApiImpl][syncSaleFlow] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }
}
