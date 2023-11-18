package com.yiling.framework.common.util;

import java.util.concurrent.CompletableFuture;

import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.MDC;

import lombok.extern.slf4j.Slf4j;

/**
 * Dubbo 工具类
 *
 * @author: xuan.zhou
 * @date: 2022/3/29
 */
@Slf4j
public class DubboUtils {

    /**
     * 异步调用跟踪 <br/>
     * 针对 @DubboReference(async = true) 引用方式
     *
     * @author xuan.zhou
     * @date 2022/3/29
     **/
    public static void quickAsyncCall(String serviceName, String methodName) {
        String traceId = MDC.get(Constants.TRACE_ID);
        // 拿到调用的Future引用，当结果返回后，会被通知和设置到此Future
        CompletableFuture<Object> future = RpcContext.getContext().getCompletableFuture();
        // 为Future添加回调
        future.whenComplete((retValue, exception) -> {
            if (exception != null) {
                MDC.put(Constants.TRACE_ID, traceId);
                log.error("Dubbo异步方法调用失败 : {}.{} {}", serviceName, methodName, exception.getMessage(), exception);
            }
        });
    }
}
