package com.yiling.admin.cms.meeting.vo;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会议管理 VO
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MeetingVO extends BaseVO {

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 封面图key
     */
    @ApiModelProperty("封面图key")
    private String backgroundPic;

    /**
     * 封面图Url
     */
    @ApiModelProperty("封面图Url")
    private String backgroundPicUrl;

    /**
     * 是否立即发布：0-否 1-是
     */
    @ApiModelProperty("是否立即发布：0-否 1-是")
    private Integer publishFlag;

    /**
     * 活动开始时间
     */
    @ApiModelProperty("活动开始时间")
    private Date activityStartTime;

    /**
     * 活动结束时间
     */
    @ApiModelProperty("活动结束时间")
    private Date activityEndTime;

    /**
     * 报名结束时间
     */
    @ApiModelProperty("报名结束时间")
    private Date applyEndTime;

    /**
     * 活动形式：1-线上 2-线下
     */
    @ApiModelProperty("活动形式：1-线上 2-线下")
    private Integer activityModus;

    /**
     * 是否有学分：0-否 1-是
     */
    @ApiModelProperty("是否有学分：0-否 1-是")
    private Integer creditFlag;

    /**
     * 学分值/人
     */
    @ApiModelProperty("学分值/人")
    private Integer creditValue;

    /**
     * 活动类型：1-科室会 2-圆桌会 3-络病大会 4-患者教育讲座 5-其它
     */
    @ApiModelProperty("活动类型：1-科室会 2-圆桌会 3-络病大会 4-患者教育讲座 5-其它（字典：meeting_activity_type）")
    private Integer activityType;

    /**
     * 是否公开：0-否 1-是
     */
    @ApiModelProperty("是否公开：0-否 1-是")
    private Integer publicFlag;

    /**
     * 主讲人
     */
    @ApiModelProperty("主讲人")
    private String mainSpeaker;

    /**
     * 内容详情
     */
    @ApiModelProperty("内容详情")
    private String content;

    /**
     * 活动状态：1-未发布 2-进行中 3-已结束 4-未开始 5-未发布已过期
     */
    @ApiModelProperty("活动状态：1-未发布 2-进行中 3-已结束 4-未开始 5-未发布已过期（字典：meeting_show_status）")
    private Integer status;

    /**
     * 阅读量（PV）
     */
    @ApiModelProperty("阅读量（PV）")
    private Integer readNum;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private Long createUser;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private Long updateUser;

    /**
     * 引用业务线：1-2C患者 2-医生 3-患者 4-店员 5-医代
     */
    @ApiModelProperty("引用业务线：1-2C患者 2-医生 3-患者 4-店员 5-医代（字典：display_line）")
    private List<Integer> businessLineList;

    @ApiModelProperty(value = "内容权限 1-仅登录 2-需认证通过")
    private Integer viewLimit;
}
