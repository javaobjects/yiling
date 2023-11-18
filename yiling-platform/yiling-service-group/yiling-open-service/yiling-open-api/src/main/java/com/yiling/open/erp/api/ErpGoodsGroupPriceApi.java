package com.yiling.open.erp.api;

/**
 * 批次信息
 * @author shuan
 */
public interface ErpGoodsGroupPriceApi {

    /**
     * 库存信息同步
     */
    void syncGoodsGroupPrice();

    /**
     * 根据suid软删除
     * @param suId
     * @return
     */
    Integer updateOperTypeGoodsBatchFlowBySuId(Long suId);
}
