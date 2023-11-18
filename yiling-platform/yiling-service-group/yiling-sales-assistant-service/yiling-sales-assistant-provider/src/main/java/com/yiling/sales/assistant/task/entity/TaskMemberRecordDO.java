package com.yiling.sales.assistant.task.entity;

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
 * 会员推广任务记录
 * </p>
 *
 * @author gxl
 * @date 2021-12-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_task_member_record")
public class TaskMemberRecordDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    private Long taskId;

    /**
     * 终端id
     */
    private Long eid;

    /**
     * 终端名称
     */
    private String ename;

    /**
     * 联系人
     */
    private String contactorPhone;

    /**
     * 联系人id
     */
    private Long contactorUserId;

    /**
     * 用户承接任务id
     */
    private Long userTaskId;

    /**
     * 下单时间或者购买时间
     */
    private Date tradeTime;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 8-会员推广-购买 9-会员推广-满赠
     */
    private Integer finishType;

    /**
     * 会员购买订单号
     */
    private String orderNo;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;

}
