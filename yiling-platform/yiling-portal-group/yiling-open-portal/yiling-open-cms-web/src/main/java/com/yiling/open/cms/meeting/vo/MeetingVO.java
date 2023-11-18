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
public class MeetingVO extends BaseVO {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("封面图")
    private String backgroundPic;

    @ApiModelProperty("活动开始时间")
    private Date activityStartTime;

    @ApiModelProperty("活动结束时间")
    private Date activityEndTime;

    @ApiModelProperty("是否有学分：0-否 1-是")
    private Integer creditFlag;

    @ApiModelProperty("学分值/人")
    private Integer creditValue;

    @ApiModelProperty("主讲人")
    private String mainSpeaker;

    @ApiModelProperty("内容详情")
    private String content;

    @ApiModelProperty("阅读量（PV）")
    private Integer readNum;

    @ApiModelProperty("报名结束时间")
    private Date applyEndTime;

    @ApiModelProperty("活动状态：1-未发布 2-已发布（进行中、已结束、未开始）")
    private Integer status;

    @ApiModelProperty("查看权限：1-仅登录 2-需认证通过")
    private Integer viewLimit;

    @ApiModelProperty(value = "收藏状态：1-收藏 2-取消收藏")
    private Integer collectStatus;
}
