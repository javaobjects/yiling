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
 * 
 * </p>
 *
 * @author gxl
 * @date 2021-09-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_task_register_terminal")
public class SaTaskRegisterTerminalDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 用户任务id
     */
    private Long userTaskId;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * enterprise主键
     */
    private Long terminalId;

    /**
     * 终端名称
     */
    private String terminalName;

    /**
     * 终端联系人
     */
    private String contactor;

    /**
     * 终端联系人手机号
     */
    private String contactorMobile;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;


}
