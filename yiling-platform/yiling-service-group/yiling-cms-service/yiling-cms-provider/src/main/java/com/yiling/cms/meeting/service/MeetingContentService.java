package com.yiling.cms.meeting.service;

import com.yiling.cms.meeting.entity.MeetingContentDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 会议内容表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-06
 */
public interface MeetingContentService extends BaseService<MeetingContentDO> {

    /**
     * 根据会议ID获取内容详情
     *
     * @param meetingId
     * @return
     */
    MeetingContentDO getContentByMeetingId(Long meetingId);

}
