package com.yiling.framework.common.exception;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.Getter;

/**
 * 抽象异常
 *
 * @author xuan.zhou
 * @date 2020-8-7
 */
@Getter
public abstract class AbstractException extends RuntimeException {

    private static final long serialVersionUID = 7271727860140180116L;

    protected IErrorCode resultCode;

    protected Integer code;
    protected String message;

    protected AbstractException(IErrorCode resultCode, Throwable e) {
        super(resultCode.getMessage(), e);
        this.resultCode = resultCode;
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    protected AbstractException(IErrorCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    protected AbstractException(IErrorCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
        this.code = resultCode.getCode();
        this.message = message;
    }

    protected AbstractException(){
    }

    public IErrorCode getResultCode() {
        return resultCode;
    }
}
