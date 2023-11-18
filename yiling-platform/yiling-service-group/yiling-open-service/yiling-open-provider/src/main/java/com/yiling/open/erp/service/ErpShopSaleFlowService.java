package com.yiling.open.erp.service;

import java.util.List;

import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.request.ErpFlowSealedUnLockRequest;
import com.yiling.open.erp.entity.ErpShopSaleFlowDO;

/**
 * <p>
 * 连锁门店销售明细信息表 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-03-20
 */
public interface ErpShopSaleFlowService {

    boolean onlineData(BaseErpEntity baseErpEntity);

    void syncShopSaleFlow();

    List<ErpShopSaleFlowDO> synSaleFlowPage();

    void unLockSynShopSaleFlow(ErpFlowSealedUnLockRequest request);
}
