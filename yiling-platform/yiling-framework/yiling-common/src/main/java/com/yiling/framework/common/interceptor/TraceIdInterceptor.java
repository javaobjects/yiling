package com.yiling.framework.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.TraceIdUtil;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import reactor.util.annotation.Nullable;

/**
 * 日志追踪标识拦截器
 * 
 * @author xuan.zhou
 * @date 2021/11/25
 */
@Slf4j
public class TraceIdInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从网关过来的请求header中拿到traceId
        String traceId = request.getHeader(Constants.TRACE_ID);
        // 不走网关时，需要在此生成
        if (StrUtil.isEmpty(traceId)) {
            traceId = TraceIdUtil.genTraceId();
        }
        // 设置日志追踪标识
        MDC.put(Constants.TRACE_ID, traceId);
        // 放入RPC作用域进行服务间的传递
        RpcContext.getContext().setAttachment(Constants.TRACE_ID, traceId);
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        MDC.clear();
    }

}