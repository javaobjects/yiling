package com.yiling.dataflow.order.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/5/27
 */
@Data
@Accessors(chain = true)
public class UpdateFlowPurchaseInventoryQuantityRequestDetail implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 库存数量变更值
     */
    private BigDecimal quantity;

}
