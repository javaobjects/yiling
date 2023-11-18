package com.yiling.job.executor.service.jobhandler;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.job.executor.log.JobLog;
import com.yiling.open.third.api.FlowInterfaceConfigApi;
import com.yiling.user.enterprise.api.CustomerApi;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: shuang.zhang
 * @date: 2022/4/12
 */
@Component
@Slf4j
public class ErpInterfaceFlowJobHandler {

    @DubboReference
    private FlowInterfaceConfigApi flowInterfaceConfigApi;

    @JobLog
    @XxlJob("executeFlowInterfaceHandler")
    public ReturnT<String> executeFlowInterfaceControl(String param) throws Exception {
        log.info("任务开始：流向接口对接任务同步");
        long start = System.currentTimeMillis();
        flowInterfaceConfigApi.executeFlowInterface();
        log.info("任务结束：流向接口对接任务同步。耗时：" + (System.currentTimeMillis() - start));
        return ReturnT.SUCCESS;
    }
}
