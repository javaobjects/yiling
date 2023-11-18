package com.yiling.sales.assistant.task.service.strategy;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;

import lombok.extern.log4j.Log4j2;

/**
 * @author:gxl
 * @description:
 * @date: Created in 21:48 2021/9/22
 * @modified By:
 */
@Log4j2
@Service
public class TaskStrategyContext  implements InitializingBean {

    @Autowired
    private List<AbstractTaskProgressHandler> taskProgressHandlers;

    public Map<Integer, AbstractTaskProgressHandler> taskProgressHandlerMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("任务进度处理器初始化 taskProgressHandlers={}", JSON.toJSONString(taskProgressHandlers));
        taskProgressHandlerMap = Maps.newHashMap();
        taskProgressHandlers.forEach(handler->{
            taskProgressHandlerMap.put(handler.getCurrentType(),handler);
        });
    }
}
