package com.yiling.hmc.wechat.api;

import java.util.List;

import com.yiling.hmc.wechat.dto.InsuranceFetchPlanDTO;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceFetchPlanRequest;

/**
 * 拿药计划API
 *
 * @Author fan.shen
 * @Date 2022/3/31
 */
public interface InsuranceFetchPlanApi {

    /**
     * 保存拿药计划
     *
     * @param requestList
     * @return
     */
    Boolean saveFetchPlan(List<SaveInsuranceFetchPlanRequest> requestList);

    /**
     * 根据参保记录id获取拿药计划
     * @param insuranceRecordId
     * @return
     */
    List<InsuranceFetchPlanDTO> getByInsuranceRecordId(Long insuranceRecordId);

    /**
     * 根据交费记录id获取拿药计划
     * @param recordPayId
     * @return
     */
    List<InsuranceFetchPlanDTO> getByRecordPayId(Long recordPayId);

    /**
     * 获取最近一次待拿药计划
     * @param insuranceRecordId
     * @return
     */
    InsuranceFetchPlanDTO getLatestPlan(Long insuranceRecordId);

    /**
     * 更新拿药状态
     * @param latestPlan
     * @return
     */
    boolean updateFetchStatus(InsuranceFetchPlanDTO latestPlan);
}
