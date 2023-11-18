package com.yiling.user.common.util.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 时间格式 ge条件查询时使用，相当于 queryWrapper.ge();
 *
 * @author lun.yu
 * @date 2022-04-07
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Before {

    /**
     * 默认启用DateUtil.beginOfDay()方法 处理时间，例如：2022-04-08 00:00:00
     *
     * @return
     */
    boolean beginOfDay() default true;

    /**
     * 字段名
     *
     * @return
     */
    String name();
}
