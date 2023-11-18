package com.yiling.cms.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.cms.meeting.entity.MeetingContentDO;
import com.yiling.cms.meeting.dao.MeetingContentMapper;
import com.yiling.cms.meeting.service.MeetingContentService;
import com.yiling.framework.common.base.BaseServiceImpl;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 会议内容表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-06
 */
@Service
public class MeetingContentServiceImpl extends BaseServiceImpl<MeetingContentMapper, MeetingContentDO> implements MeetingContentService {

    @Override
    public MeetingContentDO getContentByMeetingId(Long meetingId) {
        LambdaQueryWrapper<MeetingContentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MeetingContentDO::getMeetingId, meetingId);
        wrapper.last("limit 1");
        return this.getOne(wrapper);
    }
}
