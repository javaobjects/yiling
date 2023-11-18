package com.yiling.job.executor.service.jobhandler;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.dataflow.backup.api.AgencyBackupApi;
import com.yiling.dataflow.backup.api.RelationShipBackupApi;
import com.yiling.dataflow.backup.dto.request.AgencyBackRequest;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.job.executor.log.JobLog;
import com.yiling.sjms.gb.api.GbOrgMangerApi;

import lombok.extern.slf4j.Slf4j;

/**
 * 三者关系表备份后，更新数据
 */
@Component
@Slf4j
public class RelationShipBackupJobHandler {
    @DubboReference
    private GbOrgMangerApi gbOrgMangerApi;

    // 修改后停止改定时任务
    @DubboReference(async = true)
    private RelationShipBackupApi relationShipBackupApi;

    @JobLog
    @XxlJob("relationShipBackupJobHandler")
    public ReturnT<String> AgencyBackupJobHandler(String param) throws Exception {
       //默认执行上一个的备份
        String command = XxlJobHelper.getJobParam();
        int offset=0;
        if(StringUtils.isNotBlank(command)){
            offset=Integer.parseInt(command);
        }
        log.info("任务开始：更新三者关系基础信息，relationShipBackupJobHandler");
        List<Long> orgIds = gbOrgMangerApi.listOrgIds();
        AgencyBackRequest agencyRequest=new AgencyBackRequest();
        agencyRequest.setOffsetMonth(offset);
        relationShipBackupApi.RelationShipBackup(agencyRequest,orgIds);
        DubboUtils.quickAsyncCall("relationShipBackupApi","RelationShipBackup");
        return ReturnT.SUCCESS;
    }
}
