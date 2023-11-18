package com.yiling.open.erp.service;

import com.yiling.open.erp.bo.BaseErpEntity;

/**
 * <p>
 * 入库单明细表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2022-03-18
 */
public interface ErpOrderPurchaseDeliveryService {

    boolean onlineData(BaseErpEntity baseErpEntity);

    void syncOrderPurchaseDelivery();

}
