package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.hmc.wechat.api.InsuranceRecordApi;
import com.yiling.job.executor.log.JobLog;

import lombok.extern.slf4j.Slf4j;

/**
 * 保单过期提醒
 * @author: gx
 * @date: 2022/4/27
 */
@Component
@Slf4j
public class InsuranceExpireHandler {

    @DubboReference
    InsuranceRecordApi insuranceRecordApi;

    @JobLog
    @XxlJob("insuranceExpireNotify")
    public ReturnT<String> insuranceExpireNotify(String param) throws Exception {
        insuranceRecordApi.expireNotify();
        return ReturnT.SUCCESS;
    }
}