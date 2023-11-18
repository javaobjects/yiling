package com.yiling.hmc.wechat.api.impl;

import com.yiling.hmc.wechat.dto.request.*;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.wechat.api.InsuranceRecordApi;
import com.yiling.hmc.wechat.dto.InsuranceRecordDTO;
import com.yiling.hmc.wechat.service.InsuranceRecordService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author fan.shen
 * @Date 2022/3/26
 */
@Slf4j
@DubboService
public class InsuranceRecordApiImpl implements InsuranceRecordApi {

    @Autowired
    private InsuranceRecordService insuranceRecordService;

    @Override
    public Long saveInsuranceRecord(SaveInsuranceRecordRequest request) {
        return insuranceRecordService.saveInsuranceRecord(request);
    }

    @Override
    public InsuranceRecordDTO getByPolicyNo(String policyNo) {
        return insuranceRecordService.getByPolicyNo(policyNo);
    }

    @Override
    public Boolean updatePolicyEndTime(UpdateInsuranceRecordRequest request) {
        return insuranceRecordService.updatePolicyEndTime(request);
    }

    @Override
    public Page<InsuranceRecordDTO> pageList(QueryInsuranceRecordPageRequest request) {
        return insuranceRecordService.pageList(request);
    }

    @Override
    public InsuranceRecordDTO getById(Long id) {
        return insuranceRecordService.getById(id);
    }

    @Override
    public boolean hasInsurance(Long userId) {
        return insuranceRecordService.hasInsurance(userId);
    }

    @Override
    public void expireNotify() {
        insuranceRecordService.expireNotify();
    }

    @Override
    public boolean checkInsuranceRecord(String policyNo, Long insuranceCompanyId) {
        return insuranceRecordService.checkInsuranceRecord(policyNo, insuranceCompanyId);
    }

    @Override
    public Long joinNotify(InsuranceJoinNotifyContext context) {
        return insuranceRecordService.joinNotify(context);
    }

    @Override
    public void statusAsync(SaveInsuranceRetreatRequest request) {
        insuranceRecordService.statusAsync(request);
    }

    @Override
    public void payNotify(InsurancePayNotifyContext payNotifyContext) {
        insuranceRecordService.payNotify(payNotifyContext);
    }

    @Override
    public String uploadPolicyFile(InsuranceRecordDTO insuranceRecordDTO) {
        return insuranceRecordService.uploadPolicyFile(insuranceRecordDTO);
    }
}
