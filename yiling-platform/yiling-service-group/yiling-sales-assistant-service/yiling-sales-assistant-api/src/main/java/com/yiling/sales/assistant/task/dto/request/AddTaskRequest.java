package com.yiling.sales.assistant.task.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 创建任务
 * @author: ray
 * @date: 2021/9/13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddTaskRequest extends BaseRequest {

    private static final long serialVersionUID = 2667295634896673447L;

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
     * 状态 0未开始1进行中2已结束3停用
     */
    private Integer taskStatus;



    /**
     * 企业任务投放部门个数统计
     */
    private String taskDeptUser;
    /**
     * 任务规则
     */
    private List<AddTaskRuleRequest> addTaskRuleList;
    /**
     * 任务区域
     */
    private List<AddTaskAreaRequest> addTaskAreaList;
    /**
     * 任务商品
     */
    private List<AddTaskGoodsRelationRequest> addTaskGoodsRelationList;
    /**
     * 任务承接部门人员
     */
    private List<AddTaskDeptUserRequest> addTaskDeptUserList;

    /**
     * 配送商
     */
    private List<AddTaskDistributorRequest> addTaskDistributorList;
    /**
     * 会员信息
     */
    private AddTaskMemberRequest addTaskMember;

    /**
     * 活动信息
     */
    private AddTaskMemberPromotionRequest addTaskMemberPromotion;

    /**
     * 任务类型：1-交易类 2-推广类
     */
    private Integer category;

    /**
     * 任务类型-子类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广 8-会员推广-购买 10-上传资料
     */
    private Integer finishType;

    /**
     * 部门
     */
    private List<Long> deptIdList;

    /**
     * 企业类型 多个逗号分隔
     */
    private String enterpriseType;

}