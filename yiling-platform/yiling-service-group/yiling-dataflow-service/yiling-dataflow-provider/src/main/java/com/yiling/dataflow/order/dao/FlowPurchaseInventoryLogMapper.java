package com.yiling.dataflow.order.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.order.bo.FlowPurchaseInventorySumAdjustmentQuantityBO;
import com.yiling.dataflow.order.dto.request.QueryFlowPurchaseInventoryLogListPageRequest;
import com.yiling.dataflow.order.entity.FlowPurchaseInventoryLogDO;
import com.yiling.framework.common.base.BaseMapper;

/**
 * <p>
 * 流向商品库存日志 Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2022-05-27
 */
@Repository
public interface FlowPurchaseInventoryLogMapper extends BaseMapper<FlowPurchaseInventoryLogDO> {

    Page<FlowPurchaseInventoryLogDO> pageByInventoryId(Page<FlowPurchaseInventoryLogDO> page, @Param("request") QueryFlowPurchaseInventoryLogListPageRequest request);

    List<FlowPurchaseInventorySumAdjustmentQuantityBO> getSumAdjustmentQuantityByInventoryIdList(@Param("inventoryIdList") List<Long> inventoryIdList);

    List<FlowPurchaseInventorySumAdjustmentQuantityBO> getSumDeductionsQuantityByInventoryIdList(@Param("inventoryIdList") List<Long> inventoryIdList);

}
