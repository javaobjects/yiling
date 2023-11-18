package com.yiling.hmc.wechat.api.impl;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.wechat.api.InsuranceRecordApi;
import com.yiling.hmc.wechat.api.InsuranceRecordPayApi;
import com.yiling.hmc.wechat.dto.InsuranceRecordPayDTO;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceRecordPayRequest;
import com.yiling.hmc.wechat.dto.request.SaveInsuranceRecordRequest;
import com.yiling.hmc.wechat.entity.InsuranceRecordPayDO;
import com.yiling.hmc.wechat.service.InsuranceRecordPayService;
import com.yiling.hmc.wechat.service.InsuranceRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 主动支付
 * @Description
 * @Author fan.shen
 * @Date 2022/3/26
 */
@Slf4j
@DubboService
public class InsuranceRecordPayApiImpl implements InsuranceRecordPayApi {

    @Autowired
    private InsuranceRecordPayService insuranceRecordPayService;

    @Override
    public Long saveInsuranceRecordPay(SaveInsuranceRecordPayRequest request) {
        return insuranceRecordPayService.saveInsuranceRecordPay(request);
    }

    @Override
    public List<InsuranceRecordPayDTO> queryByInsuranceRecordId(Long insuranceRecordId) {
        return insuranceRecordPayService.queryByInsuranceRecordId(insuranceRecordId);
    }

    @Override
    public InsuranceRecordPayDTO queryById(Long recordPayId) {
        return insuranceRecordPayService.queryById(recordPayId);
    }
}
