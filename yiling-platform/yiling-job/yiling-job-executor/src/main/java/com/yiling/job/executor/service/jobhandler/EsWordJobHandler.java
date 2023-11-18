package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.search.word.api.EsWordExpansionApi;

import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 EsWordJobHandler
 * @描述
 * @创建时间 2022/4/28
 * @修改人 shichen
 * @修改时间 2022/4/28
 **/
@Slf4j
@Component
public class EsWordJobHandler {

    @DubboReference(async = true)
    EsWordExpansionApi esWordExpansionApi;

    @JobLog
    @XxlJob("syncWordJobHandler")
    public ReturnT<String> syncWordJobHandler(String param) throws Exception {
        log.info("同步历史数据词库索引开始");
        esWordExpansionApi.syncEsWordDic();
        log.info("同步历史数据词库索引结束");
        return ReturnT.SUCCESS;
    }
}
