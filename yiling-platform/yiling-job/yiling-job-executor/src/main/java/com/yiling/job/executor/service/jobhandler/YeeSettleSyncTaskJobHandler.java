package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.settlement.report.api.ReportOrderSyncApi;
import com.yiling.settlement.yee.api.YeeSettleSyncRecordApi;

import lombok.extern.slf4j.Slf4j;

/**
 * 定时同步易宝回款记录所需的同步任务
 *
 * @author dexi.yao
 * @date 2021-12-13
 */
@Component
@Slf4j
public class YeeSettleSyncTaskJobHandler {

	@DubboReference()
    YeeSettleSyncRecordApi yeeSettleSyncRecordApi;

	@JobLog
	@XxlJob("YeeSettleInitTaskJobHandler")
	public ReturnT<String> initData(String param) {
		String paramStr = XxlJobHelper.getJobParam();
		log.info("初始化易宝回款记录:任务开始", paramStr);
        yeeSettleSyncRecordApi.initData();
		log.info("初始化易宝回款记录:任务结束");
		return ReturnT.SUCCESS;
	}

	@JobLog
	@XxlJob("yeeSettleSyncTaskJobHandler")
	public ReturnT<String> syncYeeSettle(String param) {
		String paramStr = XxlJobHelper.getJobParam();
		log.info("定时同步易宝回款记录所需的同步任务:定时任务开始", paramStr);
        yeeSettleSyncRecordApi.syncMemberSettleForToday();
		log.info("定时同步易宝回款记录所需的同步任务:定时任务结束");
		return ReturnT.SUCCESS;
	}
}
