package com.yiling.open.erp.api;

/**
 * @author: shuang.zhang
 * @date: 2022/2/22
 */
public interface ErpGoodsBatchFlowApi {

    /**
     * 采购同步信息同步
     */
    void syncGoodsBatchFlow();

    Integer updateOperTypeGoodsBatchFlowBySuId(Long suId);
}
