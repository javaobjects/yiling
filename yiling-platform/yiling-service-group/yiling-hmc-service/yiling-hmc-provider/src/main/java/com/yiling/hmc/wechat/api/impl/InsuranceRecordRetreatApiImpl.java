package com.yiling.hmc.wechat.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.hmc.insurance.bo.InsuranceRecordRetreatBO;
import com.yiling.hmc.wechat.api.InsuranceRecordRetreatApi;
import com.yiling.hmc.wechat.dto.InsuranceRecordRetreatDTO;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceRetreatRequest;
import com.yiling.hmc.wechat.service.InsuranceRecordRetreatService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author fan.shen
 * @Date 2022/3/26
 */
@Slf4j
@DubboService
public class InsuranceRecordRetreatApiImpl implements InsuranceRecordRetreatApi {

    @Autowired
    private InsuranceRecordRetreatService retreatService;

    @Override
    public Long saveInsuranceRecordRetreat(SaveInsuranceRetreatRequest request) {
        return retreatService.saveInsuranceRecordRetreat(request);
    }

    @Override
    public InsuranceRecordRetreatDTO getByInsuranceRecordId(Long insuranceRecordId) {
        return retreatService.getByInsuranceRecordId(insuranceRecordId);
    }

    @Override
    public InsuranceRecordRetreatBO getRetreatDetail(Long insuranceRecordId) {
        return retreatService.getRetreatDetail(insuranceRecordId);
    }
}
