package com.yiling.open.erp.api;

/**
 * 批次信息
 * @author shuan
 */
public interface ErpGoodsCustomerPriceApi {

    /**
     * eas库存信息同步
     */
    Boolean syncEasGoodsCustomerPrice();

    /**
     * 库存信息同步
     */
    void syncGoodsCustomerPrice();

    /**
     * 大运河定价同步
     * @return
     */
    Boolean syncDayunheGoodsCustomerPrice();

    /**
     * 根据suid软删除
     * @param suId
     * @return
     */
    Integer updateOperTypeGoodsBatchFlowBySuId(Long suId);

}
