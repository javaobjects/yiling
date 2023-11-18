package com.yiling.dataflow.sale.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RemoveSaleTargetRequest extends BaseRequest {

    /**
     * 销售指标ID
     */
    private Long id;
}
