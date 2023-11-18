package com.yiling.open.erp.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.open.erp.api.ErpOrderSendApi;
import com.yiling.open.erp.service.ErpOrderSendService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@DubboService
@Slf4j
public class ErpOrderSendApiImpl implements ErpOrderSendApi {

    @Autowired
    private ErpOrderSendService erpOrderSendService;

    @Override
    public void syncOrderSend() {
        try {
            erpOrderSendService.syncOrderSend();
        } catch (Exception e) {
            log.error("[ErpOrderSendApiImpl][syncOrderSend] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }
}
