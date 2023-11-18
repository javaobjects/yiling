package com.yiling.dataflow.sale.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sale.api.SaleDepartmentSubTargetDetailApi;
import com.yiling.dataflow.sale.bo.SaleDepartmentSubTargetDetailBO;
import com.yiling.dataflow.sale.dto.request.QuerySaleDeptSubTargetDetailRequest;
import com.yiling.dataflow.sale.service.SaleDepartmentSubTargetDetailService;

/**
 * @author: gxl
 * @date: 2023/4/12
 */
@DubboService
public class SaleDepartmentSubTargetDetailApiImpl implements SaleDepartmentSubTargetDetailApi {

    @Autowired
    private SaleDepartmentSubTargetDetailService saleDepartmentSubTargetDetailService;

    @Override
    public Page<SaleDepartmentSubTargetDetailBO> queryPage(QuerySaleDeptSubTargetDetailRequest request) {
        return saleDepartmentSubTargetDetailService.queryPage(request);
    }
}