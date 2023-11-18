package com.yiling.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.bi.resource.api.UploadResourceApi;
import com.yiling.job.executor.log.JobLog;
import com.yiling.user.system.api.AdminApi;
import com.yiling.user.system.bo.Admin;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author: shuang.zhang
 * @date: 2022/9/26
 */
@Slf4j
@Component
public class DataBIHandler {

    @DubboReference
    UploadResourceApi uploadResourceApi;

    @JobLog
    @XxlJob("dataBIUploadResourceJobHandler")
    public ReturnT<String> dataBIUploadResourceJobHandler(String param) throws Exception {
//        uploadResourceApi.importInputLsflRecord("");
        return ReturnT.SUCCESS;
    }
}
