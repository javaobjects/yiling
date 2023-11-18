package com.yiling.user.common.util.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * in条件，filed must array or list
 *
 * @author: lun.yu
 * @date: 2022-04-07
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface In {

    String name() default "";
}
