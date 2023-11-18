package com.yiling.admin.hmc.activity.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/09/07
 */
@Data
@Accessors(chain = true)
public class HmcActivityDoctorVO {

    @ApiModelProperty(value = "活动id")
    private Integer activityId;

    @ApiModelProperty(value = "医生id")
    private Integer doctorId;

    @ApiModelProperty(value = "医生姓名")
    private String name;

    @ApiModelProperty(value = "医院名称")
    private String hospitalName;

    @ApiModelProperty(value = "邀请病历数量")
    private Long caseCount;

    @ApiModelProperty(value = "活动码")
    private String qrcodeUrl;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "医生是否已参加活动 1：是 2：否")
    private Integer activityState;
}
