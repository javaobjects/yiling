package com.yiling.framework.common.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.yiling.framework.common.log.enums.BusinessTypeEnum;

/**
 * 操作日志注解
 *
 * @author xuan.zhou
 * @date 2021/6/11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface Log {

    /**
     * 描述
     *
     * @return
     */
    String title() default "";

    /**
     * 业务类型
     *
     * @return
     */
    BusinessTypeEnum businessType() default BusinessTypeEnum.OTHER;

    /**
     * 是否保存日志数据
     *
     * @return
     */
    boolean save() default true;

}
