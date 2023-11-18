package com.yiling.hmc.wechat.api.impl;

import com.yiling.hmc.wechat.api.InsuranceRecordPayPlanApi;
import com.yiling.hmc.wechat.dto.InsuranceRecordPayPlanDTO;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceRecordPayPlanRequest;
import com.yiling.hmc.wechat.service.InsuranceRecordPayPlanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 续费计划API
 * @Description
 * @Author fan.shen
 * @Date 2022/3/26
 */
@Slf4j
@DubboService
public class InsuranceRecordPayPlanApiImpl implements InsuranceRecordPayPlanApi {

    @Autowired
    private InsuranceRecordPayPlanService insuranceRecordPayPlanService;

    @Override
    public Boolean saveInsuranceRecordPayPlan(List<SaveInsuranceRecordPayPlanRequest> request) {
        return insuranceRecordPayPlanService.saveInsuranceRecordPayPlan(request);
    }

    @Override
    public List<InsuranceRecordPayPlanDTO> getByPolicyNo(String policyNo) {
        return insuranceRecordPayPlanService.getByPolicyNo(policyNo);
    }

    @Override
    public Boolean hasPayPlan(Long insuranceRecordId) {
        return insuranceRecordPayPlanService.hasPayPlan(insuranceRecordId);
    }

    @Override
    public List<InsuranceRecordPayPlanDTO> getByInsuranceRecordId(Long insuranceRecordId) {
        return insuranceRecordPayPlanService.getByInsuranceRecordId(insuranceRecordId);
    }
}
