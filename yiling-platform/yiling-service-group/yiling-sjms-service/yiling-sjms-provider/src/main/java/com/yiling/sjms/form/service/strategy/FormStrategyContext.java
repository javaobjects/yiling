package com.yiling.sjms.form.service.strategy;

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
public class FormStrategyContext implements InitializingBean {

    @Autowired
    private List<AbstractFormStatusHandler> abstractFormStatusHandlers;

    public Map<Integer, AbstractFormStatusHandler> taskProgressHandlerMap;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("表单状态处理器初始化 taskProgressHandlers={}", JSON.toJSONString(abstractFormStatusHandlers));
        taskProgressHandlerMap = Maps.newHashMap();
        abstractFormStatusHandlers.forEach(handler->{
            taskProgressHandlerMap.put(handler.getCurrentType(),handler);
        });
    }
}
