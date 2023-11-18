package com.yiling.framework.common.aspect;

import com.yiling.framework.common.annotations.DynamicName;
import com.yiling.framework.common.context.DynamicTableNameHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * 切面。用户解析spel表达式，并且赋值到 {@link DynamicTableNameHelper}中
 *
 * @author: pinlin
 */
@Slf4j
public class DynamicNameAspect {
    /**
     * spel解析器
     */
    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();


    /**
     * 方法形参名解析器
     */
    private static final DefaultParameterNameDiscoverer NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    @Around("@annotation(dynamicName)")
    public Object around(ProceedingJoinPoint joinPoint, DynamicName dynamicName) throws Throwable {
        try {
            // 获取到spel表达式为空，就不需要解析了
            if (!StringUtils.hasText(dynamicName.spel())) {
                return joinPoint.proceed();
            }

            Object[] args = joinPoint.getArgs();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            String[] paramNames = NAME_DISCOVERER.getParameterNames(method);

            Expression expression = EXPRESSION_PARSER.parseExpression(dynamicName.spel());

            EvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < args.length; i++) {
                assert paramNames != null;
                context.setVariable(paramNames[i], args[i]);
            }
            String tableName = expression.getValue(context, String.class);
            if (StringUtils.hasText(tableName)) {
                DynamicTableNameHelper.set(tableName);
            }
            return joinPoint.proceed();
        } finally {
            // 释放缓存内容
            DynamicTableNameHelper.remove();
        }
    }
}
