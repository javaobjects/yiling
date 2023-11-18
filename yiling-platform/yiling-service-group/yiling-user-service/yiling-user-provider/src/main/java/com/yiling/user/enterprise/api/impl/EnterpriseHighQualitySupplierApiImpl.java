package com.yiling.user.enterprise.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.user.enterprise.api.EnterpriseHighQualitySupplierApi;
import com.yiling.user.enterprise.service.EnterpriseHighQualitySupplierService;

/**
 * 优质商家 API 实现
 *
 * @author: lun.yu
 * @date: 2023-05-10
 */
@DubboService
public class EnterpriseHighQualitySupplierApiImpl implements EnterpriseHighQualitySupplierApi {

    @Autowired
    EnterpriseHighQualitySupplierService enterpriseHighQualitySupplierService;


    @Override
    public List<Long> getAllSupplier() {
        return enterpriseHighQualitySupplierService.getAllSupplier();
    }

    @Override
    public List<Long> getByEidList(List<Long> eidList) {
        return enterpriseHighQualitySupplierService.getByEidList(eidList);
    }

    @Override
    public boolean getHighQualitySupplierFlag(Long eid) {
        return enterpriseHighQualitySupplierService.getHighQualitySupplierFlag(eid);
    }

    @Override
    public List<Long> getByProvinceCode(String provinceCode) {
        return enterpriseHighQualitySupplierService.getByProvinceCode(provinceCode);
    }
}
