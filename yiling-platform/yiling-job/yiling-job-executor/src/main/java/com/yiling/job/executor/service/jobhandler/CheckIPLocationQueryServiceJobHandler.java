package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.basic.location.api.IPLocationApi;
import com.yiling.bi.resource.api.UploadResourceApi;
import com.yiling.job.executor.log.JobLog;

import lombok.extern.slf4j.Slf4j;

/**
 * 检查IP归属地服务
 *
 * @author xuan.zhou
 * @date 2022/10/19
 **/
@Slf4j
@Component
public class CheckIPLocationQueryServiceJobHandler {

    @DubboReference
    IPLocationApi ipLocationApi;

    @JobLog
    @XxlJob("checkIPLocationQueryServiceJobHandler")
    public ReturnT<String> dataBIUploadResourceJobHandler(String param) throws Exception {
        ipLocationApi.check();
        return ReturnT.SUCCESS;
    }
}
