package com.yiling.open.third.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.open.third.api.FlowInterfaceConfigApi;
import com.yiling.open.third.service.FlowInterfaceConfigService;

/**
 * @author: shuang.zhang
 * @date: 2022/4/12
 */
@DubboService
public class FlowInterfaceConfigApiImpl implements FlowInterfaceConfigApi {

    @Autowired
    private FlowInterfaceConfigService flowInterfaceConfigService;

    @Override
    public void executeFlowInterface() {
        flowInterfaceConfigService.executeFlowInterface();
    }
}
