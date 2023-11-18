package com.yiling.job.executor.log;

import org.apache.dubbo.rpc.RpcContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.xxl.job.core.context.XxlJobHelper;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.TraceIdUtil;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;

/**
 * Job日志切面
 * 
 * @author xuan.zhou
 * @date 2021/8/16
 */
@Aspect
@Component
public class JobLogAspect {

    @Pointcut(value = "@annotation(com.yiling.job.executor.log.JobLog)")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 计时器
        TimeInterval timer = DateUtil.timer();

        // 追踪ID
        String traceId = TraceIdUtil.genTraceId();
        XxlJobHelper.log("任务开始，traceId：{}", traceId);

        MDC.put(Constants.TRACE_ID, traceId);
        RpcContext.getContext().setAttachment(Constants.TRACE_ID, traceId);

        try {
            return joinPoint.proceed();
        } finally {
            XxlJobHelper.log("任务结束，耗时：{}{}", timer.interval(), "ms");
            MDC.clear();
        }
    }
}
