package com.yiling.dataflow.sale.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sale.api.SaleDepartmentSubTargetResolveDetailApi;
import com.yiling.dataflow.sale.bo.SaleDepartmentSubTargetResolveDetailBO;
import com.yiling.dataflow.sale.dto.request.ImportSubTargetResolveDetailRequest;
import com.yiling.dataflow.sale.dto.request.QueryResolveDetailPageRequest;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetResolveDetailService;

/**
 * @author: gxl
 * @date: 2023/4/14
 */
@DubboService
public class SaleDepartmentSubTargetResolveDetailApiImpl implements SaleDepartmentSubTargetResolveDetailApi {

    @Autowired
    private SaleDepartmentSubTargetResolveDetailService  saleDepartmentSubTargetResolveDetailService;

    @Override
    public Page<SaleDepartmentSubTargetResolveDetailBO> queryPage(QueryResolveDetailPageRequest request) {
        return saleDepartmentSubTargetResolveDetailService.queryPage(request);
    }

    @Override
    public Boolean importSubTargetResolveDetail(List<ImportSubTargetResolveDetailRequest> request) {

        return saleDepartmentSubTargetResolveDetailService.importSubTargetResolveDetail(request);
    }
}