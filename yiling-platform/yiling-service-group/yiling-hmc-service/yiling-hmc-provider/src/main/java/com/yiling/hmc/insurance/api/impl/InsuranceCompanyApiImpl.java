package com.yiling.hmc.insurance.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.insurance.api.InsuranceCompanyApi;
import com.yiling.hmc.insurance.dto.InsuranceCompanyDTO;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanyDeleteRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanyListRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanyPageRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanySaveRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceCompanyStatusRequest;
import com.yiling.hmc.insurance.entity.InsuranceCompanyDO;
import com.yiling.hmc.insurance.service.InsuranceCompanyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InsuranceCompanyApiImpl implements InsuranceCompanyApi {

    private final InsuranceCompanyService insuranceCompanyService;

    @Override
    public InsuranceCompanyDTO queryById(Long id) {
        InsuranceCompanyDO insuranceCompanyDO = insuranceCompanyService.getById(id);
        return PojoUtils.map(insuranceCompanyDO, InsuranceCompanyDTO.class);
    }

    @Override
    public List<InsuranceCompanyDTO> listByCondition(InsuranceCompanyListRequest request) {
        List<InsuranceCompanyDO> doList = insuranceCompanyService.listByCondition(request);
        return PojoUtils.map(doList, InsuranceCompanyDTO.class);
    }

    @Override
    public boolean saveInsuranceCompany(InsuranceCompanySaveRequest request) {
        return insuranceCompanyService.saveInsuranceCompany(request);
    }

    @Override
    public Page<InsuranceCompanyDTO> pageList(InsuranceCompanyPageRequest request) {
        Page<InsuranceCompanyDO> doPage = insuranceCompanyService.pageList(request);
        return PojoUtils.map(doPage, InsuranceCompanyDTO.class);
    }

    @Override
    public boolean deleteInsuranceCompany(InsuranceCompanyDeleteRequest request) {
        return insuranceCompanyService.deleteInsuranceCompany(request);
    }

    @Override
    public boolean modifyStatus(InsuranceCompanyStatusRequest request) {
        return insuranceCompanyService.modifyStatus(request);
    }

    @Override
    public List<InsuranceCompanyDTO> listByIdList(List<Long> insuranceCompanyIdList) {
        return insuranceCompanyService.listByIdList(insuranceCompanyIdList);
    }
}
