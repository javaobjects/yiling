package com.yiling.dataflow.sale.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;

@Data
public class QuerySaleDepartmentSubTargetRequest extends BaseRequest {
    /**
     * 指标ID
     */
    private Long saleTargetId;

    /**
     * 部门ID
     */
    private Long departId;

    /**
     * 指标配置类型1-月度2-省区3-品种4区办
     */
    private Integer type;

}
