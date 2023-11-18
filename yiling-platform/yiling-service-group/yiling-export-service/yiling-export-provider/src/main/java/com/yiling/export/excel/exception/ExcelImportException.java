package com.yiling.export.excel.exception;

/**
 * mq 消息发送异常类
 *
 * @author xuan.zhou
 * @date 2020/8/13
 */
public class ExcelImportException extends RuntimeException {

    private static final long serialVersionUID = -5414558050379948642L;

    public ExcelImportException() {
        super();
    }

    public ExcelImportException(String message) {
        super(message);
    }

    public ExcelImportException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelImportException(Throwable cause) {
        super(cause);
    }

    protected ExcelImportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
