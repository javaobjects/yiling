package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.job.executor.log.JobLog;
import com.yiling.open.backup.api.ErpFlowBackupApi;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: houjie.sun
 * @date: 2022/7/12
 */
@Component
@Slf4j
public class ErpBackupJobHandler {

    @DubboReference(async = true)
    private ErpFlowBackupApi erpFlowBackupApi;

    @JobLog
    @XxlJob("cleanErpFlowOssFileJobHandler")
    public ReturnT<String> cleanErpFlowOssFileJobHandler(String param) throws Exception {
        log.info("任务开始：ERP流向OSS文件清理任务执行");
        erpFlowBackupApi.cleanErpFlowOssFile();
        DubboUtils.quickAsyncCall("erpFlowBackupApi","cleanErpFlowOssFile");
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("erpFlowBackupJobHandler")
    public ReturnT<String> erpFlowBackupJobHandler(String param) throws Exception {
        log.info("任务开始：ERP流向备份清理任务执行");
        erpFlowBackupApi.erpFlowBackupNew();
        return ReturnT.SUCCESS;
    }
}
