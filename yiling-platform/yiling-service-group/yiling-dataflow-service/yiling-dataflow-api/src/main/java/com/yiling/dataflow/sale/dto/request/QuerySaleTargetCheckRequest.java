package com.yiling.dataflow.sale.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;

@Data
public class QuerySaleTargetCheckRequest extends BaseRequest {
    private String name;
}
