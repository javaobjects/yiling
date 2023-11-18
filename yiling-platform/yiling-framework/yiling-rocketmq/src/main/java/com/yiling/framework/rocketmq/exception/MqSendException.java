package com.yiling.framework.rocketmq.exception;

/**
 * mq 消息发送异常类
 *
 * @author xuan.zhou
 * @date 2020/8/13
 */
public class MqSendException extends RuntimeException {

    private static final long serialVersionUID = -5414558050379948642L;

    public MqSendException() {
        super();
    }

    public MqSendException(String message) {
        super(message);
    }

    public MqSendException(String message, Throwable cause) {
        super(message, cause);
    }

    public MqSendException(Throwable cause) {
        super(cause);
    }

    protected MqSendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
