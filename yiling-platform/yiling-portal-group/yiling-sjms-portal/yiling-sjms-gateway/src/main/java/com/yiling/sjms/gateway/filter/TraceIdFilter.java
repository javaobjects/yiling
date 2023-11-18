package com.yiling.sjms.gateway.filter;

import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.TraceIdUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * TraceID 注入
 *
 * @author: xuan.zhou
 * @date: 2022/3/18
 */
@Slf4j
@Component
public class TraceIdFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = TraceIdUtil.genTraceId();
        exchange.getRequest().mutate().header(Constants.TRACE_ID, traceId).build();
        MDC.put(Constants.TRACE_ID, traceId);
        RpcContext.getContext().setAttachment(Constants.TRACE_ID, traceId);
        log.debug("requestUri:{}", exchange.getRequest().getURI().getPath());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
