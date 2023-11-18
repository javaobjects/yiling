package com.yiling.basic.mq.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * MQ发送中的消息 DTO
 *
 * @author: xuan.zhou
 * @date: 2022/1/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MqMessageSendingDTO extends BaseDTO {

    /**
     * 日志追踪ID
     */
    private String traceId;

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
    private Integer delayLevel;

    /**
     * 消息顺序ID
     */
    private Integer orderId;

    /**
     * 消息状态：1-发送中 2-发送成功 3-发送失败
     */
    private Integer status;

    /**
     * 发送失败信息
     */
    private String failedMsg;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 重试发送次数
     */
    private Integer retryTimes;

    /**
     * 下次重试时间
     */
    private Date nextRetryTime;
}
