package com.yiling.bi.order.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yiling.framework.common.base.BaseDO;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/9/16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BiOrderBackupTaskDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

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
    private Integer delFlag;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 备注
     */
    private String remark;
}
