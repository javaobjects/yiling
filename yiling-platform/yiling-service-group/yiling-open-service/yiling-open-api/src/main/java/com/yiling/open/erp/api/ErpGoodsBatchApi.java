package com.yiling.open.erp.api;

/**
 * 批次信息
 * @author shuan
 */
public interface ErpGoodsBatchApi {

    /**
     * 批次信息同步
     */
    void syncGoodsBatch();

    /**
     * eas库存信息同步
     */
    Boolean syncEasGoodsBatch();

    Integer updateOperTypeGoodsBatchFlowBySuId(Long suId);

}
