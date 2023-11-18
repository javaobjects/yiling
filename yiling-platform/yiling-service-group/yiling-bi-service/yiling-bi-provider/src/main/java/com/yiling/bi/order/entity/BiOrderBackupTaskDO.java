package com.yiling.bi.order.entity;

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
 * @author fucheng.bai
 * @date 2022/9/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("bi_order_backup_task")
public class BiOrderBackupTaskDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 任务编号
     */
    private String taskCode;

    /**
     * 备份月份
     */
    private String backupMonth;

    /**
     * 备份状态，0-未备份 1-正在备份 2-备份成功 3-备份失败
     */
    private Integer backupStatus;

    /**
     * 备份开始时间
     */
    private Date startTime;

    /**
     * 备份结束时间
     */
    private Date endTime;

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
