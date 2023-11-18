package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.bi.order.api.BiCrmOrderJobApi;
import com.yiling.bi.order.api.BiOrderBackupJobApi;
import com.yiling.dataflow.flow.api.FlowCrmSaleApi;
import com.yiling.job.executor.log.JobLog;
import com.yiling.user.system.bo.Admin;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2022/10/19
 */
@Slf4j
@Component
public class BiCrmOrderJobHandler {

    @DubboReference
    private BiCrmOrderJobApi biCrmOrderJobApi;

    @DubboReference
    private BiOrderBackupJobApi biOrderBackupJobApi;

    @DubboReference
    private FlowCrmSaleApi flowCrmSaleApi;

    @JobLog
    @XxlJob("flowCrmSaleImportHandler")
    public ReturnT<String> flowCrmSaleImportHandler(String param) throws Exception {
        flowCrmSaleApi.handleCrmSaleFile();
        return ReturnT.SUCCESS;
    }


    @JobLog
    @XxlJob("biOrderBackupJobHandler")
    public ReturnT<String> biOrderBackupJobHandler(String param) throws Exception {
        biOrderBackupJobApi.backupBiOrderData();
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("biCrmOrderJobHandler")
    public ReturnT<String> biCrmOrderJobHandler(String param) throws Exception {
        biCrmOrderJobApi.handleFtpCrmOrderData();
        return ReturnT.SUCCESS;
    }
}
