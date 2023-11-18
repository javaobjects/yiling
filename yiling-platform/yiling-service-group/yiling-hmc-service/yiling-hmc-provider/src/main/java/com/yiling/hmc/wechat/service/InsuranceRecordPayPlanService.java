package com.yiling.hmc.wechat.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.wechat.dto.InsuranceRecordPayPlanDTO;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceRecordPayPlanRequest;
import com.yiling.hmc.wechat.entity.InsuranceRecordPayPlanDO;

import java.util.List;

/**
 * <p>
 * C端参保缴费计划表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-03-30
 */
public interface InsuranceRecordPayPlanService extends BaseService<InsuranceRecordPayPlanDO> {

    /**
     * 保存缴费计划
     * @param request
     * @return
     */
    Boolean saveInsuranceRecordPayPlan(List<SaveInsuranceRecordPayPlanRequest> request);

    /**
     * 根据保单号查询续费计划
     * @return
     */
    List<InsuranceRecordPayPlanDTO> getByPolicyNo(String policyNo);

    /**
     * 是否存在待续期的支付计划
     * @param insuranceRecordId
     * @return
     */
    Boolean hasPayPlan(Long insuranceRecordId);

    /**
     * 根据保单记录id查询
     * @param insuranceRecordId
     * @return
     */
    List<InsuranceRecordPayPlanDTO> getByInsuranceRecordId(Long insuranceRecordId);
}
