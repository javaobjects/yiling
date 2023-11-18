package com.yiling.hmc.wechat.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;
import com.yiling.hmc.patient.dto.request.SavePatientRequest;
import lombok.Data;

import java.util.List;

/**
 * 主动支付回调上下文
 */
@Data
public class InsurancePayNotifyContext extends BaseRequest {

    /**
     * 支付记录
     */
    SaveInsuranceRecordPayRequest payRequest;

    /**
     * 更新保单
     */
    UpdateInsuranceRecordRequest updateInsuranceRequest;

    /**
     * 拿药计划
     */
    List<SaveInsuranceFetchPlanRequest> fetchPlanRequestList;


}
