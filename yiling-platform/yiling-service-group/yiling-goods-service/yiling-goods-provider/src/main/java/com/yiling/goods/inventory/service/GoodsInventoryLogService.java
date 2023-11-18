package com.yiling.goods.inventory.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.goods.inventory.dto.request.InventoryLogRequest;
import com.yiling.goods.inventory.entity.GoodsInventoryLogDO;

/**
 * <p>
 *  库存日志记录
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-09-02
 */
public interface GoodsInventoryLogService extends BaseService<GoodsInventoryLogDO> {

    /**
     * 记录库存变动日志
     * @param inventoryLogRequest
     * @return
     */
    boolean insertGoodInventoryLog(InventoryLogRequest inventoryLogRequest);

}
