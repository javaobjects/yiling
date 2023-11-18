package com.yiling.sales.assistant.app.mr.meeting.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会议列表项 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MeetingListItemVO extends BaseVO {

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 封面图
     */
    @ApiModelProperty("封面图")
    private String backgroundPic;

    /**
     * 活动状态：1-未发布 2-进行中 3-已结束 4-未开始 5-未发布已过期
     */
    @ApiModelProperty("活动状态：1-未发布 2-进行中 3-已结束 4-未开始 5-未发布已过期（字典：meeting_show_status）")
    private Integer status;

    /**
     * 会议时间
     */
    @ApiModelProperty("会议时间")
    private Date activityStartTime;

    /**
     * 是否有学分：0-否 1-是
     */
    @ApiModelProperty("是否有学分：0-否 1-是")
    private Integer creditFlag;

}
