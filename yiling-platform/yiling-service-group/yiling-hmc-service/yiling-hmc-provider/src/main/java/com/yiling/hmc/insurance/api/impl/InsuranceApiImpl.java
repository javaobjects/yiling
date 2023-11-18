package com.yiling.hmc.insurance.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.insurance.api.InsuranceApi;
import com.yiling.hmc.insurance.dto.InsuranceDTO;
import com.yiling.hmc.insurance.dto.InsuranceGoodsDTO;
import com.yiling.hmc.insurance.dto.InsurancePageDTO;
import com.yiling.hmc.insurance.dto.request.InsurancePageRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceSaveRequest;
import com.yiling.hmc.insurance.dto.request.InsuranceStatusRequest;
import com.yiling.hmc.insurance.entity.InsuranceDO;
import com.yiling.hmc.insurance.enums.InsuranceStatusEnum;
import com.yiling.hmc.insurance.service.InsuranceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InsuranceApiImpl implements InsuranceApi {

    private final InsuranceService insuranceService;

    @Override
    public boolean saveInsurance(InsuranceSaveRequest request) {
        return insuranceService.saveInsuranceAndDetail(request);
    }

    @Override
    public Page<InsurancePageDTO> pageList(InsurancePageRequest request) {
        return insuranceService.pageList(request);
    }

    @Override
    public InsuranceDTO queryById(Long id) {
        InsuranceDO insuranceDO = insuranceService.getById(id);
        return PojoUtils.map(insuranceDO, InsuranceDTO.class);
    }

    @Override
    public List<InsuranceDTO> listByCompanyIdAndStatus(Long insuranceCompanyId, InsuranceStatusEnum insuranceStatusEnum) {
        List<InsuranceDO> doList = insuranceService.listByCompanyIdAndStatus(insuranceCompanyId, insuranceStatusEnum);
        return PojoUtils.map(doList, InsuranceDTO.class);
    }

    @Override
    public List<InsuranceDTO> queryByIdList(List<Long> idList) {
        List<InsuranceDO> doList = insuranceService.listByIds(idList);
        return PojoUtils.map(doList, InsuranceDTO.class);
    }

    @Override
    public boolean modifyStatus(InsuranceStatusRequest request) {
        return insuranceService.modifyStatus(request);
    }

    @Override
    public List<InsurancePageDTO> getAll() {
        return insuranceService.getAll();
    }

    @Override
    public List<InsuranceGoodsDTO> queryGoods() {
        return insuranceService.queryGoods();
    }
}
