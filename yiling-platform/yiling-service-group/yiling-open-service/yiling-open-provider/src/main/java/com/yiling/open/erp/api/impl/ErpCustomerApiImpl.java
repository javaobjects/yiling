package com.yiling.open.erp.api.impl;


import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.open.erp.api.ErpCustomerApi;
import com.yiling.open.erp.dto.ErpCustomerDTO;
import com.yiling.open.erp.dto.request.ErpCustomerQueryRequest;
import com.yiling.open.erp.dto.request.SaveEnterpriseCustomerRequest;
import com.yiling.open.erp.service.ErpCustomerService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@DubboService
@Slf4j
public class ErpCustomerApiImpl implements ErpCustomerApi {

    @Autowired
    private ErpCustomerService erpCustomerService;

    @Override
    public void syncCustomer() {
        try {
            erpCustomerService.syncCustomer();
        } catch (Exception e) {
            log.error("[ErpGoodsApiImpl][syncCustomer] 异常！" + e.getMessage(), e);
            throw new BusinessException(ResultCode.FAILED);
        }
    }

    @Override
    public Boolean relationCustomer(SaveEnterpriseCustomerRequest request) {
        return erpCustomerService.relationCustomer(request);
    }

    @Override
    public Boolean relationCustomerByTyc(SaveEnterpriseCustomerRequest request) {
        return erpCustomerService.relationCustomerByTyc(request);
    }

    @Override
    public Boolean maintain(SaveEnterpriseCustomerRequest request) {
        return erpCustomerService.maintain(request);
    }

    @Override
    public Page<ErpCustomerDTO> QueryErpCustomerPageBySyncStatus(ErpCustomerQueryRequest request) {
        return erpCustomerService.QueryErpCustomerPageBySyncStatus(request);
    }

    @Override
    public List<ErpCustomerDTO> getErpCustomerListBySyncStatus(ErpCustomerQueryRequest request) {
        return erpCustomerService.getErpCustomerListBySyncStatus(request);
    }

    @Override
    public ErpCustomerDTO findById(Long id) {
        return erpCustomerService.findById(id);
    }

    @Override
    public Integer updateOperTypeGoodsBatchFlowBySuId(Long suId) {
        return erpCustomerService.updateOperTypeGoodsBatchFlowBySuId(suId);
    }

}
