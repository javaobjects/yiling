package com.yiling.framework.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 如果有错误的数据，显示错误数据字段信息
 *
 * @author: shuang.zhang
 * @date: 2021/5/19
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelShow {
    /**
     * 排序字段
     * @return
     */
    int order() default 0;
}
