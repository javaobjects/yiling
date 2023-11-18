package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.settlement.b2b.api.SettlementOrderSyncApi;
import com.yiling.settlement.report.api.ReportOrderSyncApi;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 定时同步返利报表所需的同步任务
 *
 * @author dexi.yao
 * @date 2021-12-13
 */
@Component
@Slf4j
public class ReportSyncTaskJobHandler {

	@DubboReference()
    ReportOrderSyncApi reportOrderSyncApi;

	@JobLog
	@XxlJob("reportSyncTaskJobHandler")
	public ReturnT<String> syncFlowOrder(String param) {
		String paramStr = XxlJobHelper.getJobParam();
		log.info("定时同步返利报表所需的同步任务:定时任务开始", paramStr);
        reportOrderSyncApi.syncFlowOrder();
		log.info("定时同步返利报表所需的同步任务:定时任务结束");
		return ReturnT.SUCCESS;
	}
}
