package com.yiling.dataflow.sale.api.impl;

import com.yiling.dataflow.sale.dto.SaleDepartmentTargetDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.sale.api.SaleDepartmentTargetApi;
import com.yiling.dataflow.sale.bo.SaleDepartmentTargetBO;
import com.yiling.dataflow.sale.dto.request.QuerySaleDepartmentTargetPageRequest;
import com.yiling.dataflow.sale.dto.request.UpdateConfigStatusRequest;
import com.yiling.dataflow.sale.enums.CrmSaleDepartmentTargetConfigStatusEnum;
import com.yiling.dataflow.sale.service.SaleDepartmentTargetService;

import java.util.List;

/**
 * @author: gxl
 * @date: 2023/4/12
 */
@DubboService
public class SaleDepartmentTargetApiImpl implements SaleDepartmentTargetApi {

    @Autowired
    private SaleDepartmentTargetService saleDepartmentTargetService;

    @Override
    public Page<SaleDepartmentTargetBO> querySaleDepartmentTargetPage(QuerySaleDepartmentTargetPageRequest request) {
        return saleDepartmentTargetService.querySaleDepartmentTargetPage(request);
    }

    @Override
    public List<SaleDepartmentTargetDTO> listBySaleTargetId(Long saleTargetId) {
        return saleDepartmentTargetService.listBySaleTargetId(saleTargetId);
    }

    @Override
    public SaleDepartmentTargetDTO listBySaleTargetIdAndDepartId(Long saleTargetId, Long departId) {
        return saleDepartmentTargetService.queryListByTargetId(saleTargetId,departId);
    }

    @Override
    public Boolean updateConfigStatus(UpdateConfigStatusRequest request) {
        return saleDepartmentTargetService.updateConfigStatus(request);
    }
}