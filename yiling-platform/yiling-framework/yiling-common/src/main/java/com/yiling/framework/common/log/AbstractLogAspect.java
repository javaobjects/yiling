package com.yiling.framework.common.log;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.log.enums.OperStatusEnum;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.IPUtils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 日志 Aspect 基类
 *
 * @author: xuan.zhou
 * @date: 2021/6/11
 */
@Slf4j
public abstract class AbstractLogAspect {

    /**
     * 开始时间
     */
    private Long startTime;

    @Pointcut("execution(* com.yiling..*Controller.*(..)) && !execution(* com.yiling..*Controller.initBinder(..))")
    public void logPointCut() {
    }

    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        startTime = System.currentTimeMillis();
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        this.handleLog(joinPoint, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        this.handleError(joinPoint, e);
    }

    protected void handleError(final JoinPoint joinPoint, final Exception e) {
        try {
            if (e == null || e instanceof BusinessException) {
                // BusinessException不做处理
                return;
            }

            SysOperLog sysOperLog = new SysOperLog();
            sysOperLog.setStatus(OperStatusEnum.FAIL.getCode());
            sysOperLog.setErrorMsg(e.getMessage());
            // 设置action动作
            sysOperLog.setBusinessType(BusinessTypeEnum.OTHER.getCode());
            // 设置标题
            sysOperLog.setTitle("执行过程中发生错误");
            // 设置请求信息
            this.setRequestInfo(sysOperLog, joinPoint);
            // 设置系统标识
            this.setSystemId(sysOperLog);

            // 打印日志
            this.printLog(sysOperLog);

            // 处理日志
            Log annotationLog = this.getAnnotationLog(joinPoint);
            if (annotationLog != null && annotationLog.save()) {
                this.process(sysOperLog);
            }
        } catch (Exception exp) {
            log.error(exp.getMessage(), exp);
        }
    }

    protected void handleLog(final JoinPoint joinPoint, Object jsonResult) {
        try {
            Log annotationLog = this.getAnnotationLog(joinPoint);
            if (annotationLog == null) {
                return;
            }

            SysOperLog sysOperLog = new SysOperLog();
            sysOperLog.setStatus(OperStatusEnum.SUCCESS.getCode());
            // 设置方法名
            this.setControllerMethodDescription(annotationLog, sysOperLog);
            // 设置请求信息
            this.setRequestInfo(sysOperLog, joinPoint);
            // 设置响应信息
            this.setResponseInfo(sysOperLog, jsonResult);
            // 设置系统标识
            this.setSystemId(sysOperLog);
            // 处理日志
            if (annotationLog != null) {
                this.process(sysOperLog);
            }

            // 打印日志
            this.printLog(sysOperLog);

            // 处理日志
            if (annotationLog.save()) {
                this.process(sysOperLog);
            }
        } catch (Exception exp) {
            log.error(exp.getMessage(), exp);
        }
    }

    /**
     * 日志处理
     *
     * @param sysOperLog
     */
    protected abstract void process(SysOperLog sysOperLog);

    /**
     * 设置系统标识
     *
     * @param sysOperLog
     */
    protected abstract void setSystemId(SysOperLog sysOperLog);

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     */
    public void setControllerMethodDescription(Log annotationLog, SysOperLog sysOperLog) {
        // 设置action动作
        sysOperLog.setBusinessType(annotationLog.businessType().getCode());
        // 设置标题
        sysOperLog.setTitle(annotationLog.title());
    }

    /**
     * 设置操作人信息
     *
     * @param sysOperLog
     */
    private void setRequestInfo(SysOperLog sysOperLog, JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        sysOperLog.setRequestId(MDC.get(Constants.TRACE_ID));
        sysOperLog.setRequestUrl(request.getRequestURI());
        sysOperLog.setRequestMethod(request.getMethod());

        // 设置请求参数
        this.setReqeustData(sysOperLog, joinPoint.getArgs());

        // 设置方法名称
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        sysOperLog.setClassMethod(StrFormatter.format("{}.{}", className, methodName));

        // 设置操作人ID、操作人企业ID
        sysOperLog.setOperId(Convert.toLong(request.getHeader(Constants.CURRENT_USER_ID)));
        sysOperLog.setOperEid(Convert.toLong(request.getHeader(Constants.CURRENT_EID)));
        sysOperLog.setOperIp(IPUtils.getIp(request));
        sysOperLog.setOpTime(new Date());

        // 执行时间（毫秒）
        sysOperLog.setConsumeTime((System.currentTimeMillis() - startTime));
    }

    private void setReqeustData(SysOperLog sysOperLog, Object argsObject) {
        if (argsObject == null) {
            return;
        }

        sysOperLog.setRequestData(JSONUtil.toJsonStr(argsObject));
    }

    private void setResponseInfo(SysOperLog sysOperLog, Object jsonResult) {
        if (jsonResult != null) {
            sysOperLog.setResponseData(JSONUtil.toJsonStr(jsonResult));
        }
    }

    private void printLog(SysOperLog sysOperLog) {
        if (log.isInfoEnabled()) {
            log.info("sysOperLog = {}", JSONUtil.toJsonStr(sysOperLog));
        }
    }

}
