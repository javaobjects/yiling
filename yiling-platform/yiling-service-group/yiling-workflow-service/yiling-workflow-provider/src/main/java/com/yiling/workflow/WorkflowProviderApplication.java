package com.yiling.workflow;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.yiling.framework.common.encrypt.EncrytablePropertyConfig;
import com.yiling.framework.common.util.SpringUtils;
import com.yiling.framework.rocketmq.annotation.EnableRocketMq;
import com.yiling.workflow.workflow.util.BpmnDeployUtil;

/**
 * XXXX 服务
 *
 * @author: xuan.zhou
 * @date: 2021/5/17
 */
@SpringBootApplication(scanBasePackages = { "com.yiling" })
@ComponentScan(value = { "com.yiling" })
@Import({ EncrytablePropertyConfig.class })
@EnableDubbo
@EnableDiscoveryClient
@EnableRocketMq
public class WorkflowProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowProviderApplication.class, args);
        SpringUtils.getBean(BpmnDeployUtil.class).deploy();
    }
}
