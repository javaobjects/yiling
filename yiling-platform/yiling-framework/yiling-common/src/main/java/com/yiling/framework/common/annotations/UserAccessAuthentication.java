package com.yiling.framework.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户访问认证注解
 *
 * @author xuan.zhou
 * @date 2021/6/18
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserAccessAuthentication {

    /**
     * 校验登录用户账号状态是否可用
     *
     * @return
     */
    boolean checkUserStatus() default false;

    /**
     * 校验登录用户企业状态是否启用
     *
     * @return
     */
    boolean checkEnterpriseStatus() default false;

    /**
     * 校验登录用户企业审核状态是否通过
     *
     * @return
     */
    boolean checkEnterpriseAuthStatus() default false;

    /**
     * 校验登录用户与企业的所属关系是否正常
     *
     * @return
     */
    boolean checkUserEnterpriseRelation() default false;
}
