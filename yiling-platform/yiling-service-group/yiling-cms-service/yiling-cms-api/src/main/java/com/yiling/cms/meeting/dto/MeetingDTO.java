package com.yiling.cms.meeting.dto;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 会议管理 DTO
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MeetingDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

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
     * 活动状态：1-未发布 2-已发布（进行中、已结束、未开始）
     */
    private Integer status;

    /**
     * 阅读量（PV）
     */
    private Integer readNum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人
     */
    private Long updateUser;

    /**
     * 引用业务线：1-2C患者 2-医生 3-患者 4-店员 5-医代
     */
    private List<Integer> businessLineList;

    /**
     * 医生端查看权限
     */
    private Integer viewLimit;
}
