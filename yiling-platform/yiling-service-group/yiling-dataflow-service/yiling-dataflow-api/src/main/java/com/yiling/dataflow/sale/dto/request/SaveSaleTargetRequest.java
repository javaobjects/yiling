package com.yiling.dataflow.sale.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveSaleTargetRequest extends BaseRequest {

    private Long id ;
    /**
     * 指标名称
     */
    private String name;

    /**
     * 指标编号
     */
    private String targetNo;

    /**
     * 指标年份
     */
    private Long targetYear;

    /**
     * 销售目标金额
     */
    private BigDecimal saleAmount;

    private List<SaveSaleDepartmentTargetRequest> departmentTargets;
}
