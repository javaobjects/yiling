package com.yiling.user.common.util.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 时间格式 le条件查询时使用，相当于 queryWrapper.le();
 *
 * @author lun.yu
 * @date 2022-04-07
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface After {

    /**
     * 默认启用DateUtil.endOfDay()方法 处理时间，例如：2022-04-08 23:59:59
     *
     * @return
     */
    boolean endOfDay() default true;

    /**
     * 字段名
     *
     * @return
     */
    String name();
}
