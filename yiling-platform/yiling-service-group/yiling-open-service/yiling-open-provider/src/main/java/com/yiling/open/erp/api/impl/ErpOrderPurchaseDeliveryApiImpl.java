package com.yiling.open.erp.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.open.erp.api.ErpOrderPurchaseDeliveryApi;
import com.yiling.open.erp.service.ErpOrderPurchaseDeliveryService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/3/21
 */
@DubboService
@Slf4j
public class ErpOrderPurchaseDeliveryApiImpl implements ErpOrderPurchaseDeliveryApi {

    @Autowired
    private ErpOrderPurchaseDeliveryService erpOrderPurchaseDeliveryService;

    @Override
    public void syncOrderPurchaseDelivery() {
        try {
            erpOrderPurchaseDeliveryService.syncOrderPurchaseDelivery();
        } catch (Exception e) {
            log.error("[ErpOrderPurchaseDeliveryApiImpl][syncOrderPurchaseDelivery] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }
}
