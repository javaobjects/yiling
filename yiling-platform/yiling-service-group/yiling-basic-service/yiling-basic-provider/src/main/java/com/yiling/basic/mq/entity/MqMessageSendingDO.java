package com.yiling.basic.mq.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * mq发送中的消息表
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-01-12
 */
@Data
@Accessors(chain = true)
@TableName("mq_message_sending")
public class MqMessageSendingDO {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

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
    @TableField("`keys`")
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
    @TableField("`status`")
    private Integer status;

    /**
     * 发送失败信息
     */
    private String failedMsg;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 重试发送次数
     */
    private Integer retryTimes;

    /**
     * 下次重试时间
     */
    private Date nextRetryTime;

}
