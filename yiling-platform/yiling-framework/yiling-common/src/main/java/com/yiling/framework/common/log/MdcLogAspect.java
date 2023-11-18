package com.yiling.framework.common.log;

import org.apache.dubbo.rpc.RpcContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.TraceIdUtil;

import cn.hutool.core.util.StrUtil;

/**
 * MDC中注入traceId值
 * 
 * @author xuan.zhou
 * @date 2020/12/14
 */
@Aspect
@Component
public class MdcLogAspect {

    @Pointcut(value = "@annotation(com.yiling.framework.common.log.MdcLog)")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        final String traceId = MDC.get(Constants.TRACE_ID);
        if (StrUtil.isEmpty(traceId)) {
            String newTraceId = TraceIdUtil.genTraceId();
            MDC.put(Constants.TRACE_ID, newTraceId);
            RpcContext.getContext().setAttachment(Constants.TRACE_ID, newTraceId);
        }

        try {
            return joinPoint.proceed();
        } finally {
            if (StrUtil.isEmpty(traceId)) {
                MDC.clear();
            }
        }
    }
}
