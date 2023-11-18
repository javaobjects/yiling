package com.yiling.framework.common.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrFormatter;

/**
 * 验证工具类
 *
 * @author: xuan.zhou
 * @date: 2021/5/25
 */
public class ValidateUtils {

    private static ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure().failFast(true)
        .buildValidatorFactory();

    /**
     * 快速失败模式验证
     */
    public static <T> String failFastValidate(T t, Class... groups) {
        Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<T>> validateResult = validator.validate(t, groups);
        if (CollUtil.isEmpty(validateResult)) {
            return null;
        }

        ConstraintViolation constraintViolation = validateResult.stream().findFirst().get();
        return StrFormatter.format("[{}]{}", constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
    }

    /**
     * 判断Long是否不为空且比指定0大
     *
     * @param value
     * @return
     */
    public static boolean greaterThanZero(Long value) {
        return greaterThan(value, 0);
    }

    /**
     * 判断Long是否不为空且比指定0大
     *
     * @param value
     * @return
     */
    public static boolean greaterThanZero(Integer value) {
        return greaterThan(value, 0);
    }

    /**
     * 判断Long是否不为空且比指定整数大
     *
     * @param number
     * @return
     */
    public static boolean greaterThan(Long value, int number) {
        return value != null && value.compareTo(Long.valueOf(number)) > 0;
    }

    public static boolean greaterThan(Integer value, int number) {
        return value != null && value > 0;
    }
}
