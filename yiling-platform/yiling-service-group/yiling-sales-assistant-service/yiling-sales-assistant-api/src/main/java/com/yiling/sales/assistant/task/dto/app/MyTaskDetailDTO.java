package com.yiling.sales.assistant.task.dto.app;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.yiling.sales.assistant.task.dto.TaskMemberDTO;

import lombok.Data;

/**
 * 我的任务详情
 * @author ray
 */
@Data
public class MyTaskDetailDTO implements Serializable {

    private Long id;

    private String taskName;

    private List<String> taskRule;

    private String commissionRule;

    private String taskDesc;

    private Integer finishType;

    private Integer neverExpires;

    private Date startTime;

    private Date endTime;

    private String takeCount;

    private String takeTimes;

    private Integer flag;

    private Integer taskType;

    private String profit;

    private Integer distributorCount;

    private TaskMemberDTO taskMember;

    private Boolean isStepTask;

    private Boolean alert;

    private String paymentMethod;

    private String isMember;

    /**
     * 企业类型 多个逗号分隔
     */
    private String enterpriseType;
}
