/*
package com.yiling.workflow.config;

import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.yiling.workflow.workflow.listener.AutoApproveListener;

import lombok.extern.slf4j.Slf4j;

*/
/**
 * @author: gxl
 * @date: 2023/3/6
 *//*

@Slf4j
@Component
@Configuration
public class InitConfig implements CommandLineRunner {

    @Autowired
    private RuntimeService runtimeService;
    */
/**
     * 自动审批
     *//*

    @Autowired
    private AutoApproveListener autoApproveListener;

    @Override
    public void run(String... args) {
        //配置监听器，监听级别为 TASK_CREATED
        runtimeService.addEventListener(autoApproveListener, FlowableEngineEventType.TASK_ASSIGNED);
        log.info("全局任务监听器配置：{}",autoApproveListener);
    }

}

*/
