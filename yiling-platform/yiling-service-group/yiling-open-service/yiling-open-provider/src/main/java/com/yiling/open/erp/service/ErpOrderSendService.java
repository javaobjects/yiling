package com.yiling.open.erp.service;

import java.util.List;

import com.yiling.open.erp.bo.BaseErpEntity;

/**
 * erp发货单同步
 *                       
 * @Filename: ErpGoodsService.java
 * @Version: 1.0
 * @Author: shuang.zhang 张爽
 *
 */
public interface ErpOrderSendService {

    boolean onlineData(BaseErpEntity baseErpEntity);

    void syncOrderSend();

    void refreshErpInventoryList(List<String> inSnList, Long eid);
}
