package com.yiling.dataflow.sale.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sale.bo.SaleDepartmentSubTargetResolveDetailBO;
import com.yiling.dataflow.sale.dto.request.ImportSubTargetResolveDetailRequest;
import com.yiling.dataflow.sale.dto.request.QueryResolveDetailPageRequest;

public interface SaleDepartmentSubTargetResolveDetailApi {

    /**
     * 分解模板分页列表
     * @param request
     * @return
     */
    Page<SaleDepartmentSubTargetResolveDetailBO> queryPage(QueryResolveDetailPageRequest request);

    /**
     * 导入指标分解
     * @param request
     * @return
     */
    Boolean importSubTargetResolveDetail(List<ImportSubTargetResolveDetailRequest> request);
}
