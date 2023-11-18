package com.yiling.framework.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 参数类型注解，与 CurrentAdminUser 类结合使用<br>
 * @date: 2020/6/28 <br>
 * @author: fei.wu <br>
 */
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {

    /**
     * 当前用户在request中的名字
     */
    String value() default "user";
}
