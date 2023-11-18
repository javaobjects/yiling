package com.yiling.hmc.config;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.yiling.framework.common.exception.DefaultExceptionAdvice;

/**
 * 异常处理
 *
 * @author xuan.zhou
 * @date 2021/5/14
 */
@RestControllerAdvice
public class ExceptionAdvice extends DefaultExceptionAdvice {
}
