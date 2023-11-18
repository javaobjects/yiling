package com.yiling.framework.common.exception;

import com.yiling.framework.common.enums.IErrorCode;

/**
 * 业务逻辑异常。<br/>
 * 通常用于业务逻辑的阻断返回。<br/>
 * 发生该异常不会打印日志信息。<br/>
 *
 * @author xuan.zhou
 * @date 2020-8-7
 */
public class BusinessException extends AbstractException {

    private static final long serialVersionUID = 412403351299106449L;

    /**
     * 不允许被使用，请使用带参数的方法构建
     */
    @Deprecated
    public BusinessException(){
    }

    public BusinessException(IErrorCode resultCode) {
        super(resultCode);
    }

    public BusinessException(IErrorCode resultCode, String message) {
        super(resultCode, message);
    }

    @Deprecated
    public BusinessException(IErrorCode resultCode, Throwable cause) {
        super(resultCode, cause);
    }
}
