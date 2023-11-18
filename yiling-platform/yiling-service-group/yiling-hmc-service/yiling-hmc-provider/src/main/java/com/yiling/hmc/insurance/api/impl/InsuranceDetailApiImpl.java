package com.yiling.hmc.insurance.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.insurance.api.InsuranceDetailApi;
import com.yiling.hmc.insurance.dto.InsuranceDetailDTO;
import com.yiling.hmc.insurance.dto.request.InsuranceDetailListRequest;
import com.yiling.hmc.insurance.entity.InsuranceDetailDO;
import com.yiling.hmc.insurance.service.InsuranceDetailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/3/24
 */
@Slf4j
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InsuranceDetailApiImpl implements InsuranceDetailApi {

    private final InsuranceDetailService insuranceDetailService;

    @Override
    public List<InsuranceDetailDTO> listByInsuranceId(Long insuranceId) {
        List<InsuranceDetailDO> doList = insuranceDetailService.listByInsuranceId(insuranceId);
        return PojoUtils.map(doList, InsuranceDetailDTO.class);
    }

    @Override
    public Page<InsuranceDetailDTO> pageByInsuranceNameAndStatus(InsuranceDetailListRequest request) {
        Page<InsuranceDetailDO> doPage = insuranceDetailService.pageByInsuranceNameAndStatus(request);
        return PojoUtils.map(doPage, InsuranceDetailDTO.class);
    }

    @Override
    public List<InsuranceDetailDTO> listByControlIdAndCompanyAndInsuranceStatus(List<Long> controlIdList, Long insuranceCompanyId, Integer insuranceStatus) {
        List<InsuranceDetailDO> doList = insuranceDetailService.listByControlIdAndCompanyAndInsuranceStatus(controlIdList, insuranceCompanyId, insuranceStatus);
        return PojoUtils.map(doList, InsuranceDetailDTO.class);
    }

    @Override
    public List<InsuranceDetailDTO> listByControlId(List<Long> controlIdList) {
        List<InsuranceDetailDO> doList = insuranceDetailService.listByControlId(controlIdList);
        return PojoUtils.map(doList, InsuranceDetailDTO.class);
    }
}
