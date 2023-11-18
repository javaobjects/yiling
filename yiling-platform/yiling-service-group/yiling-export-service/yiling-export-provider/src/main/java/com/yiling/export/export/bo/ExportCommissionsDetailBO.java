package com.yiling.export.export.bo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dexi.yao
 * @date 2021-09-26
 */
@Data
public class ExportCommissionsDetailBO {

    /**
     * 佣金明细id
     */
    private Long id;

    /**
     * 佣金记录id
     */
    private Long commissionsId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 用户任务id
     */
    private Long userTaskId;

    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 拉新企业id
     */
    private Long newEntId;

    /**
     * 拉新用户id
     */
    private Long newUserId;

    /**
     * 任务类型
     */
    private String  finishTypeName;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 佣金金额
     */
    private BigDecimal subAmount;

    /**
     * 用户名子
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String mobile;


    /**
     * 获佣人的属企业名称
     */
    private String ownershipName;

    /**
     * 获佣人的用户类型：1-以岭人员 2-小三元 3-自然人
     */
    private String userType;

    /**
     * 下线推广人
     */
    private String subordinateUserName;


}
