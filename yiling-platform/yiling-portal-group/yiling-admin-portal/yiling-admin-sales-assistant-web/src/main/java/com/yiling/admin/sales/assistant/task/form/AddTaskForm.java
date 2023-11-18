package com.yiling.admin.sales.assistant.task.form;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.form.BaseForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: ray
 * @date: 2021/9/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddTaskForm extends BaseForm {
    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    private String taskName;

    /**
     * 任务说明
     */
    @ApiModelProperty(value = "任务说明")
    private String taskDesc;



    /**
     * 任务主体 0:平台任务1：企业任务
     */
    @ApiModelProperty(value = "0:平台任务1：企业任务")
    private Integer taskType;

    /**
     * 是否长期有效 0:否1：是
     */
    @ApiModelProperty(value = "是否长期有效")
    private Integer neverExpires=0;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String ename;




    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间",required = true)
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "会员")
    private AddTaskMemberForm addTaskMember;

/*    @ApiModelProperty(value = "活动")
    private AddTaskMemberPromotionForm addTaskMemberPromotion;*/

    /**
     * 是否全国范围销售
     */
    @ApiModelProperty(value = "是否全国范围销售")
    private Integer fullCover;
    @ApiModelProperty(value = "任务区域集合")
    private List<AddTaskAreaForm> addTaskAreaList;
    @ApiModelProperty(value = "任务商品集合")
    private List<AddTaskGoodsRelationForm> addTaskGoodsRelationList;

    @ApiModelProperty(value = "任务规则 ruleKey 交易额交易量情况下COMMISSION 不需要传 ,SALE_CONDITION 必须传 拉人拉户和购买会员COMMISSION要传")
    private List<AddTaskRuleForm> addTaskRuleList;

 /*   @ApiModelProperty(value = "任务承接部门")
    private List<AddTaskDeptUserForm> addTaskDeptUserList;*/

    @ApiModelProperty(value = "佣金政策-阶梯任务传 非阶梯不传")
    private List<AddCommissionRuleForm> addCommissionRuleFormList;

    @ApiModelProperty(value = "配送商",required = false)
    private List<AddTaskDistributorForm> addTaskDistributorList;

    /**
     * 任务类型：1-交易类 2-推广类

    @ApiModelProperty(value = "父任务类型：1-交易类 2-推广类",required = true)
    @NotNull
    private Integer category; */



    @ApiModelProperty(value = "任务类型-子类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广 8-会员推广-购买 10-上传资料",required = true)
    @NotNull
    private Integer finishType;

    @ApiModelProperty(value = "部门 传最后一级")
    private List<Long> deptIdList;


    /**
     * 企业类型 多个逗号分隔
     */
    @ApiModelProperty(value = "企业类型 多个逗号分隔")
    private String enterpriseType;

}