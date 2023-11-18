package com.yiling.dataflow.order.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * @author: houjie.sun
 * @date: 2022/9/27
 */
@Data
public class FlowPurchaseInventorySumAdjustmentQuantityBO implements java.io.Serializable{

    private static final long serialVersionUID = -912738288675362201L;

    private Long inventoryId;

    /**
     * 调整总数量/返利报表扣减库存总数
     */
    private BigDecimal sumPoQuantity;

}
