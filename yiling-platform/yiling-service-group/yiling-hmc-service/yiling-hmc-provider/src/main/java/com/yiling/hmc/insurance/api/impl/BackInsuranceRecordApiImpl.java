package com.yiling.hmc.insurance.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.insurance.api.BackInsuranceRecordApi;
import com.yiling.hmc.insurance.bo.InsuranceRecordBO;
import com.yiling.hmc.insurance.dto.request.QueryBackInsuranceRecordPageRequest;
import com.yiling.hmc.wechat.service.InsuranceRecordService;

/**
 * 后台保险记录api
 * @author: gxl
 * @date: 2022/4/11
 */
@DubboService
public class BackInsuranceRecordApiImpl implements BackInsuranceRecordApi {

    @Autowired
    private InsuranceRecordService insuranceRecordService;

    @Override
    public Page<InsuranceRecordBO> queryPage(QueryBackInsuranceRecordPageRequest recordPageRequest) {
        return insuranceRecordService.queryPage(recordPageRequest);
    }
}