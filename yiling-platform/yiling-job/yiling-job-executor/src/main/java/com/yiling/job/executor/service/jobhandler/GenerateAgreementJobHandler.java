package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.mall.agreement.api.AgreementBusinessApi;
import com.yiling.settlement.b2b.api.SettlementApi;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 生成协议
 *
 * @author dexi.yao
 * @date 2021-12-13
 */
@Component
@Slf4j
public class GenerateAgreementJobHandler {

	@DubboReference(timeout = 1000*60)
    AgreementBusinessApi agreementBusinessApi;

	@JobLog
	@XxlJob("generateAgreeJobHandler")
	public ReturnT<String> generateAgree(String param) {
		String paramStr = XxlJobHelper.getJobParam();
		log.info("生成协议任务:定时任务开始:企业id={}", paramStr);
		String taskCode= ObjectUtil.isEmpty(paramStr)  ? null : String.valueOf(paramStr);
		log.info("生成协议任务编号={}",taskCode);
        agreementBusinessApi.generateAgree(taskCode);
		log.info("生成协议任务:定时任务结束");
		return ReturnT.SUCCESS;
	}
}
