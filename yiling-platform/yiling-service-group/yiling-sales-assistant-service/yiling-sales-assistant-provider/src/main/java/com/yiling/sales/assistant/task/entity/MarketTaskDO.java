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
 * 任务基本信息表 
 * </p>
 *
 * @author gxl
 * @date 2021-09-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("sa_market_task")
public class MarketTaskDO extends BaseDO {

    private static final long serialVersionUID = 1L;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务说明
     */
    private String taskDesc;

    /**
     * 0单品销售 1多品销售
     */
    private Integer saleType;

    /**
     * 任务主体 0:平台任务1：企业任务
     */
    private Integer taskType;

    /**
     * 是否长期有效 0:否1：是
     */
    private Integer neverExpires;

    /**
     * 企业名称
     */
    private String ename;

    /**
     * enterprise主键
     */
    private Long eid;

    /**
     * 投放区域省市区个数统计逗号分隔
     */
    private String taskArea;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 逻辑删除
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 状态 0未开始1进行中2已结束3停用
     */
    private Integer taskStatus;

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
     * 更新人id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 是否全国范围销售
     */
    private Integer fullCover;

    /**
     * 企业任务投放部门个数统计
     */
    private String taskDeptUser;



    /**
     * 任务类型-子类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广 8-会员推广-购买 9-上传资料
     */
    private Integer finishType;

    /**
     * 企业类型 多个逗号分隔
     */
    private String enterpriseType;
}
