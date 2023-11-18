package com.yiling.open.erp.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.open.erp.api.ErpShopSaleFlowApi;
import com.yiling.open.erp.service.ErpShopSaleFlowService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/2/22
 */
@DubboService
@Slf4j
public class ErpShopSaleFlowApiImpl implements ErpShopSaleFlowApi {

    @Autowired
    private ErpShopSaleFlowService erpShopSaleFlowService;

    @Override
    public void syncShopSaleFlow() {
        try {
            erpShopSaleFlowService.syncShopSaleFlow();
        } catch (Exception e) {
            log.error("[ErpShopSaleFlowApiImpl][syncSaleFlow] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }
}
