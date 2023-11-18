package com.yiling.user.enterprise.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.user.enterprise.api.EnterpriseSupplierApi;
import com.yiling.user.enterprise.bo.EnterpriseSupplierBO;
import com.yiling.user.enterprise.dto.request.QuerySupplierPageRequest;
import com.yiling.user.enterprise.dto.request.SaveOrDeleteSupplierRequest;
import com.yiling.user.enterprise.dto.request.UpdateSupplierRequest;
import com.yiling.user.enterprise.service.EnterpriseSupplierService;

/**
 * 供应商管理 API 实现
 *
 * @author: lun.yu
 * @date: 2023-06-15
 */
@DubboService
public class EnterpriseSupplierApiImpl implements EnterpriseSupplierApi {

    @Autowired
    EnterpriseSupplierService enterpriseSupplierService;

    @Override
    public Page<EnterpriseSupplierBO> queryListPage(QuerySupplierPageRequest request) {
        return enterpriseSupplierService.queryListPage(request);
    }

    @Override
    public boolean insertSupplier(SaveOrDeleteSupplierRequest request) {
        return enterpriseSupplierService.insertSupplier(request);
    }

    @Override
    public boolean deleteSupplier(SaveOrDeleteSupplierRequest request) {
        return enterpriseSupplierService.deleteSupplier(request);
    }

    @Override
    public boolean updateSupplier(UpdateSupplierRequest request) {
        return enterpriseSupplierService.updateSupplier(request);
    }

    @Override
    public EnterpriseSupplierBO get(Long id) {
        return enterpriseSupplierService.get(id);
    }

}
