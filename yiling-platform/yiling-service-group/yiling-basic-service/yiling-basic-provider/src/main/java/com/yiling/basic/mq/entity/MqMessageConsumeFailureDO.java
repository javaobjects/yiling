package com.yiling.basic.mq.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * mq消费失败的消息表
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-01-26
 */
@Data
@Accessors(chain = true)
@TableName("mq_message_consume_failure")
public class MqMessageConsumeFailureDO {

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
     * 消息TAGS
     */
    private String tags;

    /**
     * 消息KEYS
     */
    @TableField("`keys`")
    private String keys;

    /**
     * 消息内容
     */
    private String body;

    /**
     * 消费失败信息
     */
    private String failedMsg;

    /**
     * 是否已重试：0-否 1-是
     */
    private Integer retryFlag;

    /**
     * 处理状态：0-待处理 1-已处理
     */
    private Integer status;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;


}
