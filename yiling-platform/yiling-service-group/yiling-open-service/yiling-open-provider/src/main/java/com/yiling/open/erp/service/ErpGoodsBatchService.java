package com.yiling.open.erp.service;

import java.util.List;

import com.yiling.open.erp.bo.BaseErpEntity;

/**
 * erp商品库存同步
 *
 * @Filename: ErpGoodsService.java
 * @Version: 1.0
 * @Author: shuang.zhang 张爽
 */
public interface ErpGoodsBatchService {

     boolean onlineData(BaseErpEntity baseErpEntity);

     void syncGoodsBatch();

    void refreshErpInventoryList(List<String> inSnList, Long eid);

     Boolean syncEasGoodsBatch();

    Integer updateOperTypeGoodsBatchFlowBySuId(Long suId);
}
