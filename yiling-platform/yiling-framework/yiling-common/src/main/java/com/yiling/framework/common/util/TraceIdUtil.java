package com.yiling.framework.common.util;


import cn.hutool.core.util.IdUtil;

public class TraceIdUtil {

    private static final ThreadLocal<String> traceIdCache = new ThreadLocal<String>();

    public static String getTraceId() {
        return traceIdCache.get();
    }

    public static void setTraceId(String traceId) {
        traceIdCache.set(traceId);
    }

    public static void clear() {
        traceIdCache.remove();
    }

    public static String genTraceId() {
        return IdUtil.fastSimpleUUID();
    }

}