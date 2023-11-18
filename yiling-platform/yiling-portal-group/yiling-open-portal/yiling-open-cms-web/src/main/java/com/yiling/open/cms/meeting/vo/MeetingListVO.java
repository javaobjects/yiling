package com.yiling.open.cms.meeting.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: hongyang.zhang
 * @data: 2022/06/06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MeetingListVO extends BaseVO {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("封面图")
    private String backgroundPic;

    @ApiModelProperty("活动开始时间")
    private Date activityStartTime;

    @ApiModelProperty("是否有学分：0-否 1-是")
    private Integer creditFlag;

    @ApiModelProperty("主讲人")
    private String mainSpeaker;
}
