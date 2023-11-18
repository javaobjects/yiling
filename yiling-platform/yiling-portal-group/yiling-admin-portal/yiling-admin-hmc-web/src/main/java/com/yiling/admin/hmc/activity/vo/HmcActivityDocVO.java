package com.yiling.admin.hmc.activity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 医带患活动医生列表
 * @author: hongyang.zhang
 * @data: 2022/09/07
 */
@Data
@Accessors(chain = true)
public class HmcActivityDocVO {

    @ApiModelProperty(value = "活动id")
    private Integer activityId;

    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @ApiModelProperty(value = "医生姓名")
    private String name;

    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

    @ApiModelProperty(value = "所属省")
    private String provinceName;

    @ApiModelProperty(value = "报道总人数")
    private Long reportCount;

    @ApiModelProperty(value = "新用户报道人数")
    private Long newReportCount;

    @ApiModelProperty(value = "处方审核通过人数")
    private Long prescriptionCount;

    @ApiModelProperty(value = "邀请病历数量")
    private Long caseCount;

    @ApiModelProperty(value = "活动码")
    private String qrcodeUrl;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "是否拥有活动资格 1-正常 2-取消")
    private Integer doctorStatus;
}
