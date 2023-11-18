package com.yiling.hmc.wechat.api;

import com.yiling.hmc.wechat.dto.InsuranceRecordPayPlanDTO;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceRecordPayPlanRequest;

import java.util.List;

/**
 * 参保记录API
 *
 * @Author fan.shen
 * @Date 2022/3/26
 */
public interface InsuranceRecordPayPlanApi {

    /**
     * 保存参保记录
     *
     * @param request
     * @return
     */
    Boolean saveInsuranceRecordPayPlan(List<SaveInsuranceRecordPayPlanRequest> request);

    /**
     * 根据保单号查询续费计划
     *
     * @param policyNo
     * @return
     */
    List<InsuranceRecordPayPlanDTO> getByPolicyNo(String policyNo);

    /**
     * 根据保单号查询续费计划
     *
     * @param insuranceRecordId
     * @return
     */
    List<InsuranceRecordPayPlanDTO> getByInsuranceRecordId(Long insuranceRecordId);

    /**
     * 是否存在待续期的支付计划
     * @param insuranceRecordId
     * @return
     */
    Boolean hasPayPlan(Long insuranceRecordId);
}
