package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.settlement.b2b.api.SettlementApi;
import com.yiling.settlement.b2b.api.SettlementOrderSyncApi;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 定时同步结算单同步失败的 订单数据
 *
 * @author dexi.yao
 * @date 2021-12-13
 */
@Component
@Slf4j
public class B2bSettOderSyncJobHandler {

	@DubboReference()
    SettlementOrderSyncApi settlementOrderSyncApi;

	@JobLog
	@XxlJob("b2bSettOderSyncJobHandler")
	public ReturnT<String> generateSettlement(String param) {
		String paramStr = XxlJobHelper.getJobParam();
		log.info("定时同步结算单同步失败的:定时任务开始", paramStr);
		Long eid= ObjectUtil.isEmpty(paramStr)  ? null : Long.valueOf(paramStr);
        settlementOrderSyncApi.settOrderSyncFailData();
		log.info("定时同步结算单同步失败的:定时任务结束");
		return ReturnT.SUCCESS;
	}
}
