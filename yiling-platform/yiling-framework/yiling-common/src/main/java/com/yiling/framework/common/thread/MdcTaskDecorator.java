package com.yiling.framework.common.thread;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import lombok.extern.slf4j.Slf4j;

/**
 * MDC 装饰器
 * 用于获取父线程变量传递给子线程
 * @Description
 * @Author fan.shen
 * @Date 2022/2/25
 */
@Slf4j
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {

        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
