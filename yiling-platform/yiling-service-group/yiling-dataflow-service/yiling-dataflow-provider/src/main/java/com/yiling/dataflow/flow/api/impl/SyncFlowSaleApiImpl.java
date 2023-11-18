package com.yiling.dataflow.flow.api.impl;

import java.util.List;

import com.yiling.dataflow.flow.api.SyncFlowSaleApi;
import com.yiling.dataflow.flow.service.SyncFlowSaleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: shuang.zhang
 * @date: 2022/12/21
 */
@DubboService
@Slf4j
public class SyncFlowSaleApiImpl implements SyncFlowSaleApi {

    @Autowired
    private SyncFlowSaleService syncFlowSaleService;

    @Override
    public void insertList(List<Long> ids) {
        syncFlowSaleService.insertList(ids);
    }

    @Override
    public void syncFlowSaleSummary() {
        syncFlowSaleService.syncFlowSaleSummary();
    }
}
