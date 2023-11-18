package com.yiling.framework.common.annotations;

import java.lang.annotation.*;

/**
 * 动态表名注解
 * 通过spel表达式。自动注入到{@link DynamicTableNameHelper}中。而无需手动注入。减少代码侵入性
 *
 * @author: pinlin
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicName {
    /**
     * spel 表达式
     */
    String spel();
}
