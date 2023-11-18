package com.yiling.dataflow.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.bo.FlowPurchaseInventorySumAdjustmentQuantityBO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventoryLogListPageRequest;
import com.yiling.dataflow.order.dto.request.SaveFlowPurchaseInventoryLogRequest;
import com.yiling.dataflow.order.entity.FlowPurchaseInventoryLogDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 流向商品库存日志 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2022-05-27
 */
@Service
public interface FlowPurchaseInventoryLogService extends BaseService<FlowPurchaseInventoryLogDO> {

    Boolean saveGoodInventoryLog(SaveFlowPurchaseInventoryLogRequest request);

    /**
     * 根据eid、goodsInSn查询分页
     *
     * @param request
     * @return
     */
    Page<FlowPurchaseInventoryLogDO> pageByInventoryId(QueryFlowPurchaseInventoryLogListPageRequest request);

    /**
     * 根据采购库存id列表查询调整库存之和
     *
     * @param inventoryIdList
     * @return
     */
    List<FlowPurchaseInventorySumAdjustmentQuantityBO> getSumAdjustmentQuantityByInventoryIdList(List<Long> inventoryIdList);

    /**
     * 根据采购库存id列表查询返利报表扣减记录之和
     *
     * @param inventoryIdList
     * @return
     */
    List<FlowPurchaseInventorySumAdjustmentQuantityBO> getSumDeductionsQuantityByInventoryIdList(List<Long> inventoryIdList);

}
