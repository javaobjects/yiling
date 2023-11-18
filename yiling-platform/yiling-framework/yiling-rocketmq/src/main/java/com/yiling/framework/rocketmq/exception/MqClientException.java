package com.yiling.framework.rocketmq.exception;

/**
 * mq 客户端异常
 *
 * @author xuan.zhou
 * @date 2020/8/13
 **/

public class MqClientException extends RuntimeException {

    private static final long serialVersionUID = 4777153953304862667L;

    public MqClientException() {
        super();
    }

    public MqClientException(String message) {
        super(message);
    }

    public MqClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public MqClientException(Throwable cause) {
        super(cause);
    }

    protected MqClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}