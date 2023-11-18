package com.yiling.dataflow.sale.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sale.bo.SaleDepartmentSubTargetDetailBO;
import com.yiling.dataflow.sale.dto.request.QuerySaleDeptSubTargetDetailRequest;

public interface SaleDepartmentSubTargetDetailApi {
    /**
     * 销售指标分解-配置详情
     * @param request
     * @return
     */
    Page<SaleDepartmentSubTargetDetailBO> queryPage(QuerySaleDeptSubTargetDetailRequest request);
}
