package com.yiling.job.executor.service.jobhandler;

import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yiling.job.executor.log.JobLog;
import com.yiling.sales.assistant.task.api.TaskApi;
import com.yiling.sales.assistant.task.dto.request.AddTaskOrderRequest;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 任务数据修复
 * @author: gxl
 * @date: 2022/9/15
 */
@Component
@Slf4j
public class TaskDataFixJobHandler {
    @DubboReference
    TaskApi taskApi;

    @JobLog
    @XxlJob("taskDataFixJobHandler")
    public ReturnT<String> taskDataFixJobHandler(String param) throws Exception {
        String paramStr = XxlJobHelper.getJobParam();
        if(StrUtil.isEmpty(paramStr)){
            log.info("参数不能为空");
            return ReturnT.FAIL;
        }
        log.info("开始执行结束任务 param={}",paramStr);
        AddTaskOrderRequest order = taskApi.getOrderByNo(paramStr);
        if (Objects.nonNull(order)) {
            order.setDataFix(true);
            taskApi.handleTaskOrder(order);
        }
        log.info("结束执行结束任务");
        return ReturnT.SUCCESS;
    }
}