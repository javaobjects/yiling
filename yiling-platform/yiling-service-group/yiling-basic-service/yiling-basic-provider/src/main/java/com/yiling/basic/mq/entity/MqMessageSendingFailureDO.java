package com.yiling.basic.mq.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * mq发送失败的消息表
 * </p>
 *
 * @author xuan.zhou
 * @date 2022-10-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mq_message_sending_failure")
public class MqMessageSendingFailureDO extends BaseDO {

    private static final long serialVersionUID = 1L;

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
     * 消息延迟级别
     */
    private Integer delayLevel;

    /**
     * 消息顺序ID
     */
    private Integer orderId;

    /**
     * 发送失败信息
     */
    private String failedMsg;

    /**
     * 处理状态：1-未处理 2-已重投 3-已忽略
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
