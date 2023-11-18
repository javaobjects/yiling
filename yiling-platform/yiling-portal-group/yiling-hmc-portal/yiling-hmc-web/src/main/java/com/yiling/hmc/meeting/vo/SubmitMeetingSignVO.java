package com.yiling.hmc.meeting.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 会议签到VO
 *
 * @author: fan.shen
 * @data: 2023-03-14
 */
@Data
@ApiModel("SubmitMeetingSignVO")
public class SubmitMeetingSignVO {

    @ApiModelProperty("签到结果 1-提交签到成功，2-之前已签到")
    private Integer signResult;

    @ApiModelProperty("客户姓名")
    private String customerName;

    @ApiModelProperty("核销码")
    private String checkCode;

    @ApiModelProperty("首次签到日期")
    private Date firstSignDate;

    @ApiModelProperty("会场来源id 1、2、3...")
    private Integer meetingSourceId;

}
