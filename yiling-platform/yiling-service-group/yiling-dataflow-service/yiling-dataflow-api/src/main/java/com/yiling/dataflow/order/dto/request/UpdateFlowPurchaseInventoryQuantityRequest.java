package com.yiling.dataflow.order.dto.request;

import java.util.List;

import com.yiling.dataflow.order.enums.FlowPurchaseInventoryBusinessTypeEnum;
import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/5/27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateFlowPurchaseInventoryQuantityRequest extends BaseRequest {

    /**
     * 业务类型：flow-erp流向库存 settlement-报表计算库存
     */
    private Integer businessType;

    /**
     * 库存差值列表
     */
    private List<UpdateFlowPurchaseInventoryQuantityRequestDetail> list;

}
