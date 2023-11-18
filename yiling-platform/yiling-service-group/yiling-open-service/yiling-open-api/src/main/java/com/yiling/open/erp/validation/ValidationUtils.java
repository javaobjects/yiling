package com.yiling.open.erp.validation;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import com.yiling.open.erp.bo.BaseErpEntity;
import com.yiling.open.erp.enums.OperTypeEnum;
import com.yiling.open.erp.validation.entity.ValidationResult;
import com.yiling.open.erp.validation.group.Delete;

/**
 * @Description:
 * @Author: qi.xiong
 * @Date: 2019/5/8
 */
public class ValidationUtils {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> ValidationResult validate(T object, Class<?>[] groups) {
        ValidationResult result = new ValidationResult();

        Set<ConstraintViolation<T>> set = validator.validate(object, groups);
        if (set != null && set.size() > 0) {
            result.setError(true);
            Map<String, String> map = new HashMap<>();
            for (ConstraintViolation<T> constraintViolation : set) {
                map.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            }
            result.setErrorMap(map);
        }

        return result;
    }

    /**
     * 专用于ERP数据同步请求参数校验
     * @param object
     * @param <T>
     * @return
     */
    public static <T> ValidationResult validate(T object) {
        Objects.requireNonNull(object);

        if (object instanceof BaseErpEntity) {
            Integer operType = ((BaseErpEntity) object).getOperType();
            if (Objects.equals(operType, OperTypeEnum.DELETE.getCode())) {
                return validate(object, new Class[]{ Delete.class });
            }

            return validate(object, new Class[]{ Default.class, Delete.class });
        }

        try {
            Method method = object.getClass().getMethod("getOperType");

            Object value = method.invoke(object);
            if (Objects.equals(value, OperTypeEnum.DELETE.getCode())) {
                return validate(object, new Class[]{ Delete.class });
            }
        } catch (Exception e) {}

        return validate(object, new Class[]{ Default.class, Delete.class });
    }
}
