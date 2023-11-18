package com.yiling.hmc.remind.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用药提醒任务详情表
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("hmc_meds_remind_task_detail")
public class MedsRemindTaskDetailDO extends BaseDO {

    private static final long serialVersionUID = 1L;
//
//    /**
//     * 用药提醒任务表主键
//     */
//    private Long remindTaskId;

    /**
     * 用药提醒表主键
     */
    private Long medsRemindId;

    /**
     * 确认状态 1-已确认，2-未确认
     */
    private Integer confirmStatus;

    /**
     * 初始发送时间
     */
    private Date initSendTime;

    /**
     * 实际发送时间
     */
    private Date actualSendTime;

    /**
     * 消息发送状态：1-待发送，2-发送完成，3-发送失败，4-取消发送
     */
    private Integer sendStatus;

    /**
     * 消息接收人id
     */
    private Long receiveUserId;

    /**
     * 消息内容
     */
    private String content;

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
