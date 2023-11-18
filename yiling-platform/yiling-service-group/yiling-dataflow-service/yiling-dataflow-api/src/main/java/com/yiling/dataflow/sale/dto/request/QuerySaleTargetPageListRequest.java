package com.yiling.dataflow.sale.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;
import lombok.Data;

@Data
public class QuerySaleTargetPageListRequest extends QueryPageListRequest {
    /**
     * 销售之宝ID
     */
    private Long id;
}
