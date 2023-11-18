package com.yiling.framework.common.dubbo.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.slf4j.MDC;

import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.TraceIdUtil;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Activate(group = { CommonConstants.PROVIDER, CommonConstants.CONSUMER }, order = -9999)
public class GlobalTraceFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String traceId = RpcContext.getContext().getAttachment(Constants.TRACE_ID);
        if (StrUtil.isNotEmpty(traceId)) {
            TraceIdUtil.setTraceId(traceId);
        } else {
            traceId = TraceIdUtil.getTraceId();
            RpcContext.getContext().setAttachment(Constants.TRACE_ID, traceId);
        }

        MDC.put(Constants.TRACE_ID, traceId);
//        log.debug("traceId:{}", traceId);
        return invoker.invoke(invocation);
    }
}
