package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.hmc.patient.dto.request.SavePatientRequest;
import lombok.Data;

import java.util.List;

/**
 * 参保回调上下文
 */
@Data
public class InsuranceJoinNotifyContext extends BaseRequest {

    /**
     * 参保记录
     */
    SaveInsuranceRecordRequest insuranceRecordRequest;

    /**
     * 参保缴费计划
     */
    List<SaveInsuranceRecordPayPlanRequest> payPlanRequestList;

    /**
     * 首次缴费记录
     */
    SaveInsuranceRecordPayRequest payRequest;

    /**
     * 拿药计划
     */
    List<SaveInsuranceFetchPlanRequest> fetchPlanRequestList;

    /**
     * 拿药计划明细
     */
    List<SaveInsuranceFetchPlanDetailRequest> fetchPlanDetailRequestList;

    /**
     * 构建参保记录id
     * @param insuranceRecordId
     */
    public void buildInsuranceRecordId(Long insuranceRecordId) {

        payRequest.setInsuranceRecordId(insuranceRecordId);

        payPlanRequestList.stream().forEach(item -> item.setInsuranceRecordId(insuranceRecordId));

        fetchPlanRequestList.stream().forEach(item -> item.setInsuranceRecordId(insuranceRecordId));

        fetchPlanDetailRequestList.stream().forEach(item -> item.setInsuranceRecordId(insuranceRecordId));

    }

}
