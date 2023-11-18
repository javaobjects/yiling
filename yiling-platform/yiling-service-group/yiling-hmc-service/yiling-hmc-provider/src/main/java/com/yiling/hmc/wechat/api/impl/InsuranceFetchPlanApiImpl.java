package com.yiling.hmc.wechat.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.hmc.wechat.api.InsuranceFetchPlanApi;
import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDTO;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceFetchPlanRequest;
import com.yiling.hmc.wechat.service.InsuranceFetchPlanService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author fan.shen
 * @Date 2022/3/26
 */
@Slf4j
@DubboService
public class InsuranceFetchPlanApiImpl implements InsuranceFetchPlanApi {

    @Autowired
    private InsuranceFetchPlanService fetchPlanService;

    @Override
    public Boolean saveFetchPlan(List<SaveInsuranceFetchPlanRequest> requestList) {
        return fetchPlanService.saveFetchPlan(requestList);
    }

    @Override
    public List<InsuranceFetchPlanDTO> getByInsuranceRecordId(Long insuranceRecordId) {
        return fetchPlanService.getByInsuranceRecordId(insuranceRecordId);
    }

    @Override
    public List<InsuranceFetchPlanDTO> getByRecordPayId(Long recordPayId) {
        return fetchPlanService.getByRecordPayId(recordPayId);
    }

    @Override
    public InsuranceFetchPlanDTO getLatestPlan(Long insuranceRecordId) {
        return fetchPlanService.getLatestPlan(insuranceRecordId);
    }

    @Override
    public boolean updateFetchStatus(InsuranceFetchPlanDTO planDTO) {
        return fetchPlanService.updateFetchStatus(planDTO);
    }
}
