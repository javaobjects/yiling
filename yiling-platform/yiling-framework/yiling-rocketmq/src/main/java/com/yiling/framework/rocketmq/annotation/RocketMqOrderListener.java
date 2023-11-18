package com.yiling.framework.rocketmq.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * 有序消息监听
 *
 * @author xuan.zhou
 * @date 2020/8/13
 **/
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RocketMqOrderListener {

    String value() default "";

    String topic();

    String tag() default "*";

    int maxThread() default 20;

    String consumerGroup();
}
