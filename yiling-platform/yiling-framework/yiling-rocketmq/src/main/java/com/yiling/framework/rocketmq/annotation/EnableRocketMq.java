package com.yiling.framework.rocketmq.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.yiling.framework.rocketmq.config.RocketMqFactoryBeanConfig;
import com.yiling.framework.rocketmq.config.RocketMqProperties;
import com.yiling.framework.rocketmq.mq.RocketMqConsumerRunner;
import com.yiling.framework.rocketmq.mq.RocketMqProducerService;

/**
 * 启用RocketMQ注解
 *
 * @author xuan.zhou
 * @date 2020/8/13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
    RocketMqProperties.class,
    RocketMqConsumerRunner.class,
    RocketMqProducerService.class,
    RocketMqProperties.class,
    RocketMqFactoryBeanConfig.class
})
public @interface EnableRocketMq {
}