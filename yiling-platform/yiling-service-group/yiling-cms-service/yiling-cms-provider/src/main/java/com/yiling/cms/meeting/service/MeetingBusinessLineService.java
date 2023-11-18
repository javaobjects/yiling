package com.yiling.cms.meeting.service;

import java.util.List;

import com.yiling.cms.meeting.entity.MeetingBusinessLineDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 会议引用业务线表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-02
 */
public interface MeetingBusinessLineService extends BaseService<MeetingBusinessLineDO> {

    /**
     * 获取引用业务线
     *
     * @param meetingId
     * @return
     */
    List<MeetingBusinessLineDO> getBusinessLineByMeetingId(Long meetingId);

}
