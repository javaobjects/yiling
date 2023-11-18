package com.yiling.cms.meeting.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 保存会议 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveMeetingRequest extends BaseRequest {

    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面图
     */
    private String backgroundPic;

    /**
     * 是否立即发布：0-否 1-是
     */
    private Integer publishFlag;

    /**
     * 活动开始时间
     */
    private Date activityStartTime;

    /**
     * 活动结束时间
     */
    private Date activityEndTime;

    /**
     * 报名结束时间
     */
    private Date applyEndTime;

    /**
     * 活动形式：1-线上 2-线下
     */
    private Integer activityModus;

    /**
     * 是否有学分：0-否 1-是
     */
    private Integer creditFlag;

    /**
     * 学分值/人
     */
    private Integer creditValue;

    /**
     * 活动类型：1-科室会 2-圆桌会 3-络病大会 4-患者教育讲座 5-其它
     */
    private Integer activityType;

    /**
     * 是否公开：0-否 1-是
     */
    private Integer publicFlag;

    /**
     * 主讲人
     */
    private String mainSpeaker;

    /**
     * 内容详情
     */
    private String content;

    /**
     * 引用业务线：1-2C患者 2-医生 3-患者 4-店员 5-医代
     */
    private List<Integer> businessLineList;

    /**
     * 医生端查看权限1-仅登录 2-需认证通过
     */
    private Integer viewLimit;
}
