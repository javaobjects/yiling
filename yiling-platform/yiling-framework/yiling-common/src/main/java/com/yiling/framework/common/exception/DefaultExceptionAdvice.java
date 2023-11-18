package com.yiling.framework.common.exception;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

import org.apache.dubbo.rpc.RpcException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 统一异常处理
 *
 * @author xuan.zhou
 * @date 2019/10/15
 */
@Slf4j
public class DefaultExceptionAdvice {

    /**
     * IllegalArgumentException异常处理返回json
     */
    @ExceptionHandler({ IllegalArgumentException.class })
    public Result handleIllegalArgumentException(IllegalArgumentException e) {
        return this.defHandler("参数解析失败", e);
    }

    /**
     * AccessDeniedException异常处理返回json
     */
    @ExceptionHandler({ AccessDeniedException.class })
    public Result handleAccessDeniedException(AccessDeniedException e) {
        return this.defHandler("没有权限请求当前方法");
    }

    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return this.defHandler("不支持当前请求方法", e);
    }

    @ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
    public Result handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return this.defHandler("不支持当前媒体类型", e);
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return this.defHandler("参数错误", e);
    }

    @ExceptionHandler({ MissingServletRequestParameterException.class, MissingServletRequestPartException.class, BindException.class })
    public Result handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return this.defHandler("参数缺失", e);
    }

    /**
     * 验证异常
     */
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public Result validationErrorHandler(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        FieldError error = (FieldError) result.getAllErrors().get(0);
        String defaultMessage = error.getDefaultMessage();

        String message;
        if (defaultMessage.startsWith(Constants.VALIDATION_CUSTOM_MESSAGE_PREFIX)) {
            message = defaultMessage.replaceFirst("\\" + Constants.VALIDATION_CUSTOM_MESSAGE_PREFIX, "");
        } else {
            message = StrFormatter.format("[{}]{}", error.getField(), defaultMessage);
        }
        return this.defHandler(message);
    }

    @ExceptionHandler({ SQLException.class })
    public Result handleSQLException(SQLException e) {
        return this.defHandler("服务运行SQLException异常", e);
    }

    @ExceptionHandler({HttpMessageConversionException.class})
    public Result httpMessageConversionException(HttpMessageConversionException e){
        return this.defHandler("参数异常",e);
    }

    /**
     * BusinessException 业务异常处理
     */
    @ExceptionHandler({ BusinessException.class })
    public Result handleException(BusinessException e) {
        Integer code = Convert.toInt(e.getCode(), ResultCode.FAILED.getCode());
        String message = Convert.toStr(e.getMessage(), ResultCode.FAILED.getMessage());
        return this.defHandler(code, message);
    }

    /**
     * ServiceException 服务异常处理
     */
    @ExceptionHandler({ ServiceException.class })
    public Result handleServiceException(ServiceException e) {
        ResultCode resultCode = e.getResultCode();
        if (resultCode != null) {
            return this.defHandler(StrUtil.format("[{}]{}", resultCode.getCode(), resultCode.getMessage()), e);
        } else {
            return this.defHandler(e.getMessage(), e);
        }
    }

    /**
     * org.apache.dubbo.rpc.RpcException 通常由Dubbo服务抛出
     */
    @ExceptionHandler({ RpcException.class })
    public Result handleRpcException(RpcException e) {
        return this.defHandler("调用服务失败，请稍后再试", e);
    }

    /**
     * MaxUploadSizeExceededException 上传文件大小超过限制
     */
    @ExceptionHandler({ MaxUploadSizeExceededException.class, SizeLimitExceededException.class})
    public Result handleMaxUploadSizeExceededException(Exception e) {
        return this.defHandler("上传文件大小超过限制");
    }

    /**
     * 所有异常统一处理
     */
    @ExceptionHandler({ Exception.class })
    public Result handleException(Exception e) {
        return this.defHandler("服务异常，请稍后再试", e);
    }

    private Result defHandler(String msg) {
        return Result.failed(msg);
    }

    private Result defHandler(Integer code, String msg) {
        return Result.failed(code, msg);
    }

    private Result defHandler(String msg, Exception e) {
        log.error(msg, e);
        return Result.failed(msg);
    }
}
