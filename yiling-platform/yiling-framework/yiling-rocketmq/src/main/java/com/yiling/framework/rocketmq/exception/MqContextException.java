package com.yiling.framework.rocketmq.exception;

/**
 * MqContextException
 *
 * @author xuan.zhou
 * @date 2020/8/13
 **/
public class MqContextException extends Throwable {

    private String            messageId;
    private String            topic;
    private MqClientException exception;

    public MqContextException() {
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public MqClientException getException() {
        return this.exception;
    }

    public void setException(MqClientException exception) {
        this.exception = exception;
    }
}
