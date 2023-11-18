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
 * @date: 2021/9/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateTaskForm extends BaseForm {
    @NotNull
    @ApiModelProperty(value = "任务id")
    private Long taskId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "任务说明")
    private String taskDesc;


    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "修改原有任务规则的值")
    private List<UpdateTaskRuleForm> updateTaskRuleList;

    @ApiModelProperty(value = "任务商品")
    private List<UpdateTaskGoodsRelationForm> updateTaskGoodsRelationList;



    @ApiModelProperty(value = "投放区域")
    private List<UpdateTaskAreaForm> updateTaskAreaList;
    @ApiModelProperty(value = "承接部门")
    private List<UpdateTaskDeptUserForm> updateTaskDeptUserList;

    @ApiModelProperty(value = "是否全国范围销售")
    private Integer fullCover;

    @ApiModelProperty(value = "配送商-追加情况传")
    private List<UpdateTaskDistributorForm> updateTaskDistributorList;

    @ApiModelProperty(value = "会员-无修改不传")
    private UpdateTaskMemberForm updateTaskMember;

    @ApiModelProperty(value = "追加任务规则")
    private List<AddTaskRuleForm> addTaskRuleList;


    @ApiModelProperty(value = "佣金政策-阶梯任务传 非阶梯不传,需要将原有的值传回来")
    private List<AddCommissionRuleForm> addCommissionRuleFormList;

    /**
     * 部门
     */
    @ApiModelProperty(value = "部门，没有修改不传")
    private List<Long> deptIdList;

    /**
     * 企业类型 多个逗号分隔
     */
    @ApiModelProperty(value = "企业类型 多个逗号分隔")
    private String enterpriseType;
}