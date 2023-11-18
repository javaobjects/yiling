package com.yiling.cms.meeting.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 查询会议管理分页列表 Request
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryMeetingPageListRequest extends QueryPageListRequest {

    /**
     * 标题（like查询）
     */
    private String title;

    /**
     * 引用业务线：1-2C患者 2-医生 3-患者 4-店员 5-医代
     */
    private List<Integer> useLineList;

    /**
     * 活动状态：1-未发布（未发布、未发布已过期） 2-已发布（进行中、已结束、未开始）
     */
    private Integer status;

    /**
     * 开始创建时间
     */
    private Date startCreateTime;

    /**
     * 结束创建时间
     */
    private Date endCreateTime;

    /**
     * 是否公开：0-否 1-是
     */
    private Integer publicFlag;

    /**
     * 活动开始时间
     */
    private Date activityStartTime;

    /**
     * 活动结束时间
     */
    private Date activityEndTime;

    /**
     * 大于等于 活动结束时间（查询历史会议可使用）
     */
    private Date revertActivityEndTime;

    /**
     * 活动开始时间排序：true为倒序，false为正序
     */
    private Boolean activityStartTimeDesc;

    /**
     * 会议展示状态查询：2-进行中 3-已结束 4-未开始
     */
    private Integer showStatus;
}
