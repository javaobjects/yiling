package com.yiling.open.thirdbase.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.open.thirdbase.api.ErpBaseInterfaceConfigApi;
import com.yiling.open.thirdbase.service.ErpBaseInterfaceConfigService;

/**
 * @author: houjie.sun
 * @date: 2023/6/20
 */
@DubboService
public class ErpBaseInterfaceConfigApiImpl implements ErpBaseInterfaceConfigApi {

    @Autowired
    private ErpBaseInterfaceConfigService erpBaseInterfaceConfigService;

    @Override
    public void executeBaseInterface() {
        erpBaseInterfaceConfigService.executeBaseInterface();
    }
}
