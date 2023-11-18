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
 * 我的任务 销售承接的任务
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_user_task")
public class UserTaskDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 任务主体 0:平台任务1：企业任务
     */
    private Integer taskType;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 承接人姓名
     */
    private String userName;

    /**
     * 承接人手机号
     */
    private String mobile;

    /**
     * 任务主键
     */
    private Long taskId;

    /**
     * 任务类型：任务类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广
     */
    private Integer finishType;

    /**
     * 已完成值
     */
    private Integer finishValue;

    /**
     * 目标值（金额转成分存储）
     */
    private Integer goal;

    /**
     * 百分比
     */
    private String percent;

    /**
     * 完成任务商品数量
     */
    private Integer finishGoods;

    /**
     * 任务包涵商品总数
     */
    private Integer taskGoodsTotal;

    /**
     * 任务状态 0-进行中1-已完成2-未完成 3-停用
     */
    private Integer taskStatus;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 企业id
     */
    private Long eid;


}
