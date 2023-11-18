package com.yiling.hmc.insurance.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.insurance.api.BackInsuranceRecordPayApi;
import com.yiling.hmc.insurance.bo.InsuranceRecordPayBO;
import com.yiling.hmc.insurance.dto.request.QueryInsuranceRecordPayPageRequest;
import com.yiling.hmc.wechat.service.InsuranceRecordPayService;

/**
 * 后台查保险缴费记录api
 * @author: gxl
 * @date: 2022/4/13
 */
@DubboService
public class BackInsuranceRecordPayApiImpl implements BackInsuranceRecordPayApi {

    @Autowired
    private InsuranceRecordPayService insuranceRecordPayService;
    @Override
    public Page<InsuranceRecordPayBO> queryPage(QueryInsuranceRecordPayPageRequest request) {
        return insuranceRecordPayService.queryPage(request);
    }
}