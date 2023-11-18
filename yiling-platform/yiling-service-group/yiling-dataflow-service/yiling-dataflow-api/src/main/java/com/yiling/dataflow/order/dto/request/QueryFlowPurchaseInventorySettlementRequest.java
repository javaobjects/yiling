package com.yiling.dataflow.order.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/5/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryFlowPurchaseInventorySettlementRequest extends BaseRequest {

    /**
     * 商业公司id
     */
    private List<QueryFlowPurchaseInventorySettlementDetailRequest> list;

}
