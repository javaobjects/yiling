package com.yiling.dataflow.flow.entity;

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
 * @author shuang.zhang
 * @date 2022-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("flow_month_bi_task")
public class FlowMonthBiTaskDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    private Long monthJobId;

    /**
     * 企业ID
     */
    private Long eid;

    private Long crmEnterpriseId;

    private String ename;

    private String taskCode;

    /**
     * 任务时间
     */
    private Date taskTime;

    /**
     * 同步状态，0未同步，1正在同步 2同步成功 3同步失败
     */
    private Integer syncStatus;

    /**
     * 同步时间
     */
    private Date syncTime;

    /**
     * 同步信息
     */
    private String syncMsg;

    /**
     * 是否删除：0-否 1-是
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

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
