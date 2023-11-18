package com.yiling.dataflow.order.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/9/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowPurchaseInventoryLogListPageRequest extends QueryPageListRequest {

    /**
     * 采购库存ID
     */
    private Long inventoryId;

    /**
     * 采购库存ID
     */
    private Integer businessType;

}
