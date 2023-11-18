package com.yiling.open.erp.service;

import java.util.List;

import com.yiling.open.erp.bo.BaseErpEntity;

/**
 * erp客户定价
 *
 * @Filename: ErpGoodsService.java
 * @Version: 1.0
 * @Author: shuang.zhang 张爽
 */
public interface ErpGoodsCustomerPriceService {

     boolean onlineData(BaseErpEntity baseErpEntity);

    void refreshErpInventoryList(List<String> inSnList, Long eid);

     void syncGoodsCustomerPrice();

    Boolean syncEasGoodsCustomerPrice();

    Boolean syncDayunheGoodsCustomerPrice();

    Integer updateOperTypeGoodsBatchFlowBySuId(Long suId);
}
