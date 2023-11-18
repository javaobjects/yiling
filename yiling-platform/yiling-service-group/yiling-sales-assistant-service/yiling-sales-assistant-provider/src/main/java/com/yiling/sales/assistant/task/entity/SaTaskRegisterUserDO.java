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
 * 任务和拉新人-关联表
 * </p>
 *
 * @author gxl
 * @date 2021-09-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_task_register_user")
public class SaTaskRegisterUserDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 用户任务id
     */
    private Long userTaskId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * 注册时间
     */
    private Date registerTime;

    private Long userId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;


}
