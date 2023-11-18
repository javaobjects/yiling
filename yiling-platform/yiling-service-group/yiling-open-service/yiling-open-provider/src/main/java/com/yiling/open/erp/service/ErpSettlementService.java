package com.yiling.open.erp.service;

import com.yiling.open.erp.bo.BaseErpEntity;

/**
 * erp结算单同步
 *
 * @Filename: ErpGoodsService.java
 * @Version: 1.0
 * @Author: shuang.zhang 张爽
 */
public interface ErpSettlementService {

    boolean onlineData(BaseErpEntity baseErpEntity);

    void syncSettlement();

}
