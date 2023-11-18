package com.yiling.dataflow.flow.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/8/5
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlowBiTaskDTO extends BaseDTO {

    /**
     * 任务ID
     */
    private Long jobId;

    /**
     * 企业ID
     */
    private Long eid;

    private Long crmEnterpriseId;

    private String taskCode;

    /**
     * 任务时间
     */
    private Date taskTime;

    /**
     * 同步状态，0未同步，1正在同步 2同步成功 3同步失败 4-不需要task任务执行的
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
    private Integer delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 备注
     */
    private String remark;
}
