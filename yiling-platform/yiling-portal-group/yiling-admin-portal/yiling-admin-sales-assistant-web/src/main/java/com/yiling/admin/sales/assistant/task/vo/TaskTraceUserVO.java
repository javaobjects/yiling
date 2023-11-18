package com.yiling.admin.sales.assistant.task.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 任务追踪成员明细
 * </p>
 *
 * @author gxl
 * @since 2021-09-17
 */
@Data
public class TaskTraceUserVO implements Serializable {


    private static final long serialVersionUID = 4595719924715919419L;
    @ApiModelProperty(value = "用户主键")
    private Long userId;

    @ApiModelProperty(value = "用户姓名")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String mobile;


    @ApiModelProperty(value = "任务主键")
    private Long taskId;

    @ApiModelProperty(value = "用户任务主键")
    private Long userTaskId;


    @ApiModelProperty(value = "任务类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广")
    private Integer finishType;

    @ApiModelProperty(value = "已完成值")
    private String finishValue;

    @ApiModelProperty(value = "完成任务商品数量")
    private Integer finishGoods;

    @ApiModelProperty(value = "任务包涵商品总数")
    private Integer taskGoodsTotal;

    @ApiModelProperty(value = "目标值（金额转成分存储）")
    private String goal;

    @ApiModelProperty(value = "完成百分比")
    private String percent;

    @ApiModelProperty(value = "任务状态 0进行中1已完成2未完成 3停用")
    private Integer taskStatus;

    @ApiModelProperty(value = "是否每个品种都达到目标值")
    private Boolean eachCompute;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createdTime;

    @ApiModelProperty(value = "阶梯任务目标值各占的百分比")
    private List<String> percentList;
/*    @ApiModelProperty(value = "完成阶梯")
    private Integer finishStep;

    @ApiModelProperty(value = "佣金百分比")
    private String commision;*/
}
