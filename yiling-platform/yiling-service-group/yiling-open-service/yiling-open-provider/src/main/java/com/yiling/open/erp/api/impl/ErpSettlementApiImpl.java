package com.yiling.open.erp.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.open.erp.api.ErpSettlementApi;
import com.yiling.open.erp.service.ErpSettlementService;

/**
 * @author: shuang.zhang
 * @date: 2021/7/28
 */
@DubboService
public class ErpSettlementApiImpl implements ErpSettlementApi {

    @Autowired
    private ErpSettlementService erpSettlementService;

    @Override
    public void syncSettlement() {
        erpSettlementService.syncSettlement();
    }


}
