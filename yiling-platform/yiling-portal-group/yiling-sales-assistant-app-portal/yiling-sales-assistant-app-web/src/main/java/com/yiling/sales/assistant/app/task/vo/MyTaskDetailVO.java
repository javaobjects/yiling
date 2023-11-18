package com.yiling.sales.assistant.app.task.vo;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 我的任务详情
 * @author ray
 */
@Data
public class MyTaskDetailVO{


    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "任务条件")
    private List<String> taskRule;

    @ApiModelProperty(value = "佣金设置")
    private String commissionRule;

    @ApiModelProperty(value = "任务说明")
    private String taskDesc;

    private Integer finishType;


    @ApiModelProperty(value = "开始时间 长期有效只有开始没有结束")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;


    @ApiModelProperty(value = "领取按钮判断 1 可参与人数到达限制 2 可参与次数到达限制 3任务已承接")
    private Integer flag;

    @ApiModelProperty(value = "任务主体 0:平台任务1：企业任务")
    private Integer taskType;

    @ApiModelProperty(value = "参与配送商")
    private Integer distributorCount;

    @ApiModelProperty(value = "会员信息")
    private TaskMemberVO taskMember;

    private String profit;
    @ApiModelProperty(value = "是否阶梯任务")
    private Boolean isStepTask;

    @ApiModelProperty(value = "是否弹窗提醒---您领取的任务新增了配送商或商品")
    private Boolean alert;

    @ApiModelProperty(value = "支付方式")
    private String paymentMethod;

    @ApiModelProperty(value = "是否会员")
    private String isMember;

    /**
     * 企业类型 多个逗号分隔
     */
    @ApiModelProperty(value = "企业类型")
    private String enterpriseType;
}