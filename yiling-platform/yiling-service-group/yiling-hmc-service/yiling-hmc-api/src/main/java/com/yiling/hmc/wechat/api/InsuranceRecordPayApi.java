package com.yiling.hmc.wechat.api;

import com.yiling.hmc.wechat.dto.InsuranceRecordPayDTO;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceRecordPayRequest;

import java.util.List;

/**
 * 主动支付API
 *
 * @Author fan.shen
 * @Date 2022/3/26
 */
public interface InsuranceRecordPayApi {

    /**
     * 保存主动支付记录
     *
     * @param request
     * @return
     */
    Long saveInsuranceRecordPay(SaveInsuranceRecordPayRequest request);

    /**
     * 查询保单交费记录
     * @param insuranceRecordId
     * @return
     */
    List<InsuranceRecordPayDTO> queryByInsuranceRecordId(Long insuranceRecordId);

    /**
     * 查询交费记录
     * @param recordPayId
     * @return
     */
    InsuranceRecordPayDTO queryById(Long recordPayId);
}
