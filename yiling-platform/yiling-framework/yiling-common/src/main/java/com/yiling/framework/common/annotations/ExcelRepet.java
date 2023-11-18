package com.yiling.framework.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 验证excel里面有重复的数据，可以支持多个字段验证重复。例如 A，B  groupName="人物" C groupName="动物"
 *
 * @author: shuang.zhang
 * @date: 2021/5/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelRepet {
    /**
     * 字段
     * @return
     */
    String groupName() default "";
}
