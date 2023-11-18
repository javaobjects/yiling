package com.yiling.framework.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fucheng.bai
 * @date 2023/2/2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface CsvOrder {

    /**
     * csv文件列位置
     * @return
     */
    int value() default -1;
}
