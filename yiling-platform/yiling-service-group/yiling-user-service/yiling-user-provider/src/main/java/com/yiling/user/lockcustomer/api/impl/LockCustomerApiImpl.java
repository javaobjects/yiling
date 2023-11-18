package com.yiling.user.lockcustomer.api.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.security.DenyAll;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.lockcustomer.api.LockCustomerApi;
import com.yiling.user.lockcustomer.dao.LockCustomerMapper;
import com.yiling.user.lockcustomer.dto.LockCustomerDTO;
import com.yiling.user.lockcustomer.dto.request.AddLockCustomerRequest;
import com.yiling.user.lockcustomer.dto.request.ImportLockCustomerRequest;
import com.yiling.user.lockcustomer.dto.request.QueryLockCustomerPageRequest;
import com.yiling.user.lockcustomer.dto.request.UpdateLockCustomerStatusRequest;
import com.yiling.user.lockcustomer.entity.LockCustomerDO;
import com.yiling.user.lockcustomer.service.LockCustomerService;

/**
 * <p>
 * 锁定用户 API实现
 * </p>
 *
 * @author lun.yu
 * @date 2022-04-02
 */
@DubboService
public class LockCustomerApiImpl extends BaseServiceImpl<LockCustomerMapper, LockCustomerDO> implements LockCustomerApi {

    @Autowired
    private LockCustomerService lockCustomerService;

    @Override
    public Page<LockCustomerDTO> queryListPage(QueryLockCustomerPageRequest request) {
        return lockCustomerService.queryListPage(request);
    }

    @Override
    public List<LockCustomerDTO> queryList(QueryLockCustomerPageRequest request) {
        return lockCustomerService.queryList(request);
    }

    @Override
    public LockCustomerDTO getByLicenseNumber(String licenseNumber) {
        return lockCustomerService.getByLicenseNumber(licenseNumber);
    }

    @Override
    public Map<String, Integer> batchQueryByLicenseNumber(List<String> licenseNumberList) {
        return lockCustomerService.batchQueryByLicenseNumber(licenseNumberList);
    }

    @Override
    public boolean addLockCustomer(AddLockCustomerRequest request) {
        return lockCustomerService.addLockCustomer(request);
    }

    @Override
    public boolean updateStatus(UpdateLockCustomerStatusRequest request) {
        return lockCustomerService.updateStatus(request);
    }

    @Override
    public boolean importData(ImportLockCustomerRequest request) {
        return lockCustomerService.importData(request);
    }
}
