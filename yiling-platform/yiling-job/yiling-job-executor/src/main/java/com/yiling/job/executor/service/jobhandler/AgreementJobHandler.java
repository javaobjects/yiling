package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.user.agreement.api.AgreementApi;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shuan
 */
@Slf4j
@Component
public class AgreementJobHandler {

    @DubboReference(timeout = 1000*10)
    AgreementApi agreementApi;

    @DubboReference(async = true)
    AgreementBusinessApi agreementBusinessApi;

    @JobLog
    @XxlJob("agreementJobHandler")
    public ReturnT<String> syncAgreementRelation(String param) throws Exception {
        agreementApi.syncAgreementRelation();
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("agreementCalculateByDayJobHandler")
    public ReturnT<String> calculateRebateAgreementByDay(String param) throws Exception {
        agreementBusinessApi.calculateRebateAgreementByDay();
        return ReturnT.SUCCESS;
    }

}
