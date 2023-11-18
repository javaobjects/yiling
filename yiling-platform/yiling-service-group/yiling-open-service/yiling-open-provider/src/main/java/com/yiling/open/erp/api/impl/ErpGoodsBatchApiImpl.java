package com.yiling.open.erp.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.open.erp.api.ErpGoodsBatchApi;
import com.yiling.open.erp.service.ErpGoodsBatchService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2021/7/28
 */
@Slf4j
@DubboService
public class ErpGoodsBatchApiImpl implements ErpGoodsBatchApi {

    @Autowired
    private ErpGoodsBatchService erpGoodsBatchService;

    @Override
    public void syncGoodsBatch() {
        log.info("任务开始：库存信息同步");
        long start = System.currentTimeMillis();
         erpGoodsBatchService.syncGoodsBatch();
        log.info("任务结束：库存信息同步。耗时：" + (System.currentTimeMillis() - start));
    }

    @Override
    public Boolean syncEasGoodsBatch() {
        return erpGoodsBatchService.syncEasGoodsBatch();
    }

    @Override
    public Integer updateOperTypeGoodsBatchFlowBySuId(Long suId) {
        return erpGoodsBatchService.updateOperTypeGoodsBatchFlowBySuId(suId);
    }

}
