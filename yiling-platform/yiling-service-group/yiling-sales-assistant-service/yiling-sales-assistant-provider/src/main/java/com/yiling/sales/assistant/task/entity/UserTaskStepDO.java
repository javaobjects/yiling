package com.yiling.sales.assistant.task.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 阶梯任务-用户完成所在阶梯
 * </p>
 *
 * @author gxl
 * @date 2021-10-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_user_task_step")
public class UserTaskStepDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 阶梯
     */
    private Integer stepNo;

    /**
     * 佣金
     */
    private String commision;

    /**
     * 用户承接任务id
     */
    private Long userTaskId;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建时间
     */
    private Date createdTime;


}
