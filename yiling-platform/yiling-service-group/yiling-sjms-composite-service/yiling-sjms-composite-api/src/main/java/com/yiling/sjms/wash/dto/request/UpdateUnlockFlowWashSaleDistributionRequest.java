package com.yiling.sjms.wash.dto.request;


import java.util.List;

import com.yiling.dataflow.wash.dto.request.SaveUnlockSaleDepartmentRequest;
import com.yiling.framework.common.base.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: shuang.zhang
 * @date: 2023/5/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateUnlockFlowWashSaleDistributionRequest extends BaseRequest {

    /**
     * 销量计入规则：1-指定部门2-指定部门+省区3-商业公司三者关系4-商业公司负责人
     */
    private Integer saleRange;

    private SaveUnlockSaleDepartmentRequest saveUnlockSaleDepartmentRequest;

    /**
     * 非锁分配备注
     */
    private String notes;

    private Integer judgment;

    private List<Long> ids;
}
