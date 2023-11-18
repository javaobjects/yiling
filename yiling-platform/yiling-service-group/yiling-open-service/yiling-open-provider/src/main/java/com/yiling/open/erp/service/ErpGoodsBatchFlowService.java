package com.yiling.open.erp.service;

import com.yiling.open.erp.bo.BaseErpEntity;

/**
 * <p>
 * 库存流向明细信息表 服务类
 * </p>
 *
 * @author: houjie.sun
 * @date: 2022/2/14
 */
public interface ErpGoodsBatchFlowService {

    boolean onlineData(BaseErpEntity baseErpEntity);

    void syncGoodsBatchFlow();

    Integer updateOperTypeGoodsBatchFlowBySuId(Long suId);
}
