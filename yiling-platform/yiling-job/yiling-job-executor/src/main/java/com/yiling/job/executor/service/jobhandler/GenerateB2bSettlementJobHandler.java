package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.settlement.b2b.api.SettlementApi;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 生成b2b结算单
 *
 * @author dexi.yao
 * @date 2021-12-13
 */
@Component
@Slf4j
public class GenerateB2bSettlementJobHandler {

	@DubboReference(timeout = 1000*60)
	SettlementApi settlementApi;

	@JobLog
	@XxlJob("generateB2bSettlementJobHandler")
	public ReturnT<String> generateSettlement(String param) {
		String paramStr = XxlJobHelper.getJobParam();
		log.info("生成b2b结算单:定时任务开始:企业id={}", paramStr);
		Long eid= ObjectUtil.isEmpty(paramStr)  ? null : Long.valueOf(paramStr);
		log.info("企业id={}",eid);
		settlementApi.generateSettlementByEid(eid);
		log.info("生成b2b结算单:定时任务结束");
		return ReturnT.SUCCESS;
	}
}
