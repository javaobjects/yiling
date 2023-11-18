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
 * 任务选择的配送商
 * </p>
 *
 * @author gxl
 * @date 2021-10-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_task_distributor")
public class TaskDistributorDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 配送商
     */
    private Long distributorEid;

    /**
     * 类型：1-云仓 2-非云仓
     */
    private Integer type;

    private Long taskId;

    private String name;

    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer delFlag;


}
