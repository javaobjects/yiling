package com.yiling.job.executor.service.jobhandler;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.dataflow.backup.dto.request.AgencyBackRequest;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationPinchRunnerApi;
import com.yiling.job.executor.log.JobLog;
import com.yiling.sjms.gb.api.GbOrgMangerApi;

import lombok.extern.slf4j.Slf4j;

/**
 * 代跑三者关系表备份后，更新销量计入主管岗位信息
 *
 * @author: houjie.sun
 * @date: 2023/4/26
 */
@Component
@Slf4j
public class RelationPinchRunnerBackupJobHandler {

    @DubboReference
    private GbOrgMangerApi gbOrgMangerApi;

    @DubboReference(async = true)
    CrmEnterpriseRelationPinchRunnerApi crmEnterpriseRelationPinchRunnerApi;

    @JobLog
    @XxlJob("relationPinchRunnerBackupJobHandler")
    public ReturnT<String> relationPinchRunnerBackupJobHandler(String param) throws Exception {
        // 默认执行上一个月的备份
        String command = XxlJobHelper.getJobParam();
        int offset = 0;
        if(StringUtils.isNotBlank(command)){
            offset=Integer.parseInt(command);
        }
        log.info("任务开始：更新代跑三者关系备份表销量计入主管岗位信息, relationShipBackupJobHandler, offsetMonth:" + offset);
        // 获取事业部部门ID列表
        List<Long> orgIds = gbOrgMangerApi.listOrgIds();
        AgencyBackRequest agencyRequest=new AgencyBackRequest();
        agencyRequest.setOffsetMonth(offset);
        crmEnterpriseRelationPinchRunnerApi.relationShipPinchRunnerBackup(agencyRequest,orgIds);
        return ReturnT.SUCCESS;
    }

}
