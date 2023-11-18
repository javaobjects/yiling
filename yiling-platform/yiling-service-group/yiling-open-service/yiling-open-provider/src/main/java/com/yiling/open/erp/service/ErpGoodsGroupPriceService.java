package com.yiling.open.erp.service;

import java.util.List;

import com.yiling.open.erp.bo.BaseErpEntity;

/**
 * erp分组定价同步
 *
 * @Filename: ErpGoodsService.java
 * @Version: 1.0
 * @Author: shuang.zhang 张爽
 */
public interface ErpGoodsGroupPriceService {

     boolean onlineData(BaseErpEntity baseErpEntity);

     void syncGoodsGroupPrice();

    void refreshErpInventoryList(List<String> inSnList, Long eid);

    Integer updateOperTypeGoodsBatchFlowBySuId(Long suId);
}
