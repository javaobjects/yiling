package com.yiling.dataflow.sale.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 销售指标配置批量保存
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveBathSaleDepartmentSubTargetRequest extends BaseRequest {
    private List<SaveSaleDepartmentSubTargetRequest> provinceList;
    private List<SaveSaleDepartmentSubTargetRequest> monthList;
    private List<SaveSaleDepartmentSubTargetRequest> goodsList;
    private List<SaveSaleDepartmentSubTargetRequest> areaList;
    private Long saleTargetId;
    /**
     * 部门ID
     */
    private Long departId;
}
