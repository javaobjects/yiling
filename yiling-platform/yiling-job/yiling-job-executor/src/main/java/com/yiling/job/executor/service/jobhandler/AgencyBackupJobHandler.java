package com.yiling.job.executor.service.jobhandler;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.basic.mail.api.MailApi;
import com.yiling.dataflow.backup.api.AgencyBackupApi;
import com.yiling.dataflow.backup.api.DataFlowBackupApi;
import com.yiling.dataflow.backup.dto.request.AgencyBackRequest;
import com.yiling.dataflow.backup.dto.request.DataFlowBackupRequest;
import com.yiling.job.executor.log.JobLog;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.open.erp.dto.ErpClientDTO;
import com.yiling.open.erp.dto.request.ErpClientQueryRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 机构档案备份任务
 */
@Component
@Slf4j
public class AgencyBackupJobHandler {
    @DubboReference(async = true)
    private AgencyBackupApi agencyBackupApi;
    @JobLog
    @XxlJob("agencyBackupJobHandler")
    public ReturnT<String> agencyBackupJobHandler(String param) throws Exception {
       //默认执行上一个的备份
        String command = XxlJobHelper.getJobParam();
        int offset=0;
        if(StringUtils.isNotBlank(command)){
            offset=Integer.parseInt(command);
        }
        log.info("任务开始：线上基础档案备份表结构定时任务，AgencyBackupJobHandler");
        AgencyBackRequest agencyRequest=new AgencyBackRequest();
         agencyRequest.setOffsetMonth(offset);
        agencyBackupApi.agencyInfoBackup(agencyRequest);
        return ReturnT.SUCCESS;
    }
    @JobLog
    @XxlJob("agencyEsbBackupJobHandler")
    public ReturnT<String> agencyEsbBackupJobHandler(String param) throws Exception {
        //默认执行上一个的备份
        String command = XxlJobHelper.getJobParam();
        int offset=0;
        if(StringUtils.isNotBlank(command)){
            offset=Integer.parseInt(command);
        }
        log.info("任务开始：线上基础档案人员备份表结构定时任务，AgencyBackupJobHandler");
        AgencyBackRequest agencyRequest=new AgencyBackRequest();
        agencyRequest.setOffsetMonth(offset);
        agencyBackupApi.esbInfoBackup(agencyRequest);
        return ReturnT.SUCCESS;
    }
}
