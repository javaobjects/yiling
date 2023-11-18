package com.yiling.hmc.wechat.api;

import com.yiling.hmc.insurance.bo.InsuranceRecordRetreatBO;
import com.yiling.hmc.wechat.dto.InsuranceRecordRetreatDTO;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceRetreatRequest;


/**
 * 退保记录记录API
 *
 * @Author fan.shen
 * @Date 2022/4/6
 */
public interface InsuranceRecordRetreatApi {

    /**
     * 保存退保记录
     *
     * @param request
     * @return
     */
    Long saveInsuranceRecordRetreat(SaveInsuranceRetreatRequest request);

    /**
     * 根据参保记录查询退保记录
     * @param insuranceRecordId
     * @return
     */
    InsuranceRecordRetreatDTO getByInsuranceRecordId(Long insuranceRecordId);

    /**
     * 后台查询退保详情
     * @param insuranceRecordId
     * @return
     */
    InsuranceRecordRetreatBO getRetreatDetail(Long insuranceRecordId);

}
