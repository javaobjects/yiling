package com.yiling.job.executor;

import com.yiling.framework.rocketmq.annotation.EnableRocketMq;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

import com.yiling.framework.common.encrypt.EncrytablePropertyConfig;

/**
 * Job Executor Application
 *
 * @author xuan.zhou
 * @date 2021/5/14
 */
@SpringBootApplication(scanBasePackages = { "com.yiling" })
@Import({ EncrytablePropertyConfig.class })
@EnableDubbo
@EnableRocketMq
@EnableDiscoveryClient
public class JobExecutorApplication {

	public static void main(String[] args) {
        SpringApplication.run(JobExecutorApplication.class, args);
	}

}