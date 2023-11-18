package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.user.system.api.AdminApi;
import com.yiling.user.system.bo.Admin;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DemoJobHandler {

    @DubboReference
    AdminApi adminApi;

    @JobLog
    @XxlJob("demoJobHandler")
    public ReturnT<String> demoJobHandler(String param) throws Exception {
        Admin admin = adminApi.getById(1L);
//        XxlJobHelper.log("userInfo:" + JSONUtil.toJsonStr(admin));
        return ReturnT.SUCCESS;
    }

}
