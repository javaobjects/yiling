package com.yiling.admin.sales.assistant.task.vo;

import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 任务详情
 * @author: ray
 * @date: 2021/9/15
 */
@Data
public class TaskDetailVO {
    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "任务说明")
    private String taskDesc;

    @ApiModelProperty(value = "任务主体 0:平台任务1：企业任务")
    private Integer taskType;

/*    @ApiModelProperty(value = "0单品任务 1多品销售")
    private Integer saleType;*/

    @ApiModelProperty(value = "任务类型-子类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广 8-会员推广-购买")
    private Integer finishType;

    @ApiModelProperty(value = "更新时间")
    private Date updatedTime;

    @ApiModelProperty(value = "企业名称")
    private String ename;

    @ApiModelProperty(value = "销售区域")
    private String taskArea;

    @ApiModelProperty(value = "任务承接部门")
    private String taskDeptUser;

    @ApiModelProperty(value = "是否长期有效 0:否1：是")
    private Integer neverExpires;

    @ApiModelProperty(value = "开始时间 长期有效只有开始没有结束")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;


    @ApiModelProperty(value = "创建人")
    private String createdBy;
    @ApiModelProperty(value = "创建时间")
    private Date createdTime;
    @ApiModelProperty(value = "状态 0未开始1进行中2已结束3停用")
    private Integer taskStatus;
    @ApiModelProperty(value = "参与条件")
    private List<TaskRuleVO> takeRuleVOList;

    @ApiModelProperty(value = "任务完成条件")
    private List<TaskRuleVO> finishRuleVOList;

    @ApiModelProperty(value = "佣金设置")
    private List<TaskRuleVO> commissionRuleVOList;

    @ApiModelProperty(value = "任务商品")
    private List<TaskGoodsVO> taskGoodsList;

    @ApiModelProperty(value = "是否全国范围销售")
    private  Integer fullCover;

    @ApiModelProperty(value = "企业id")
    private Integer eid;


    @ApiModelProperty(value = "收益")
    private String profit;

    @ApiModelProperty(value = "佣金政策-阶梯任务的")
    private List<CommissionRuleVO> commRuleVOList;

    @ApiModelProperty(value = "会员")
    private TaskMemberVO taskMember;

    /**
     * 部门
     */
    @ApiModelProperty(hidden = true)
    private List<Long> deptIdList;

    @ApiModelProperty(value = "部门集合")
    private List<TaskDeptVO> taskDeptVOS;
    /**
     * 企业类型 多个逗号分隔
     */
    @ApiModelProperty(value = "企业类型 多个逗号分隔")
    private String enterpriseType;
}