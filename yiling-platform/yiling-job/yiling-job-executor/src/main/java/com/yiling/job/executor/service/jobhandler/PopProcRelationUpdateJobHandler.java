package com.yiling.job.executor.service.jobhandler;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.job.executor.log.JobLog;
import com.yiling.user.procrelation.api.ProcurementRelationApi;
import com.yiling.user.procrelation.dto.ProcurementRelationDTO;
import com.yiling.user.procrelation.dto.request.QueryProcRelationByTimePageRequest;
import com.yiling.user.procrelation.dto.request.UpdateRelationStatusRequest;
import com.yiling.user.procrelation.enums.ProcurementRelationStatusEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 定时生效或过期Pop采购关系
 *
 * @author dexi.yao
 * @date 2021-12-13
 */
@Component
@Slf4j
public class PopProcRelationUpdateJobHandler {

    @DubboReference(async = true)
    ProcurementRelationApi procurementRelationApi;

    @JobLog
    @XxlJob("updatePopProcRelationInProgress")
    public ReturnT<String> updateInProgress(String param) {
        String paramStr = XxlJobHelper.getJobParam();
        log.info("定时生效pop采购关系:定时任务开始", paramStr);
        procurementRelationApi.updateInProgress();
        log.info("定时生效pop采购关系:定时任务结束");
        return ReturnT.SUCCESS;
    }

    @JobLog
    @XxlJob("updatePopProcRelationExpired")
    public ReturnT<String> updateExpired(String param) {
        String paramStr = XxlJobHelper.getJobParam();
        log.info("定时过期pop采购关系:定时任务开始", paramStr);
        procurementRelationApi.updateExpired();
        log.info("定时过期pop采购关系:定时任务结束");
        return ReturnT.SUCCESS;
    }
}
