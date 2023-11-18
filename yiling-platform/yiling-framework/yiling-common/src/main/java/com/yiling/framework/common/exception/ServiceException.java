package com.yiling.framework.common.exception;

import com.yiling.framework.common.enums.ResultCode;

import lombok.Getter;

/**
 * 服务逻辑异常。<br/>
 * 通常在遇到正常业务逻辑发生意外情况时抛出。<br/>
 * 发生该异常会打印异常日志信息。<br/>
 *
 * @author xuan.zhou
 * @date 2020-8-7
 */
public class ServiceException extends RuntimeException {

    @Getter
    private ResultCode resultCode;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ServiceException(ResultCode resultCode, String msg) {
        super(msg);
        this.resultCode = resultCode;
    }

    public ServiceException(ResultCode resultCode, Throwable cause) {
        super(cause);
        this.resultCode = resultCode;
    }

    public ServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
