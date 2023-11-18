package com.yiling.basic.mq.bo;

import org.slf4j.MDC;

import com.yiling.framework.common.util.Constants;
import com.yiling.framework.rocketmq.enums.MqDelayLevel;

import lombok.Data;
import lombok.ToString;

/**
 * MQ消息 BO
 *
 * @author: xuan.zhou
 * @date: 2022/1/10
 */
@Data
@ToString
public class MqMessageBO implements java.io.Serializable {

    private static final long serialVersionUID = -1519450794961241373L;

    private MqMessageBO(){
    }

    public MqMessageBO(String topic, String tags, String body) {
        this(topic, tags, body, 0, null);
    }

    public MqMessageBO(String topic, String tags, String body, MqDelayLevel delayLevel) {
        this(topic, tags, body, 0, delayLevel);
    }

    public MqMessageBO(String topic, String tags, String body, Integer orderId) {
        this(topic, tags, body, orderId, null);
    }

    public MqMessageBO(String topic, String tags, String body, Integer orderId, MqDelayLevel delayLevel) {
        this.topic = topic;
        this.tags = tags;
        this.body = body;
        this.orderId = orderId;
        this.delayLevel = delayLevel;
        this.traceId = MDC.get(Constants.TRACE_ID);
    }

    /**
     * ID
     */
    private Long id;

    /**
     * 消息ID
     */
    private String msgId;

    /**
     * 消息TOPIC
     */
    private String topic;

    /**
     * 消息TAG
     */
    private String tags;

    /**
     * 消息KEY
     */
    private String keys;

    /**
     * 消息内容
     */
    private String body;

    /**
     * 消息延迟级别
     */
    private MqDelayLevel delayLevel;

    /**
     * 相同orderId会被顺序消费
     */
    private Integer orderId;

    /**
     * 日志追踪ID
     */
    private String traceId;

}
