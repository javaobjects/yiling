package com.yiling.open.erp.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.dto.request.ErpFlowSealedUnLockRequest;
import com.yiling.open.erp.dto.request.QueryErpPurchaseFlowPageRequest;
import com.yiling.open.erp.entity.ErpPurchaseFlowDO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-09-23
 */
public interface ErpPurchaseFlowService {

    boolean onlineData(BaseErpEntity baseErpEntity);

    void synPurchaseFlow();

    void unLockSynPurchaseFlow(ErpFlowSealedUnLockRequest request);

}
