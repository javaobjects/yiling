package com.yiling.open.erp.api.impl;


import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.open.erp.api.ErpFlowControlApi;
import com.yiling.open.erp.service.ErpFlowControlService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@DubboService
@Slf4j
public class ErpFlowContorApiImpl implements ErpFlowControlApi {

    @Autowired
    private ErpFlowControlService erpFlowControlService;

    @Override
    public void syncFlowControl() {
        try {
            erpFlowControlService.syncFlowControl();
        } catch (Exception e) {
            log.error("[ErpGoodsApiImpl][syncFlowControl] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

}
