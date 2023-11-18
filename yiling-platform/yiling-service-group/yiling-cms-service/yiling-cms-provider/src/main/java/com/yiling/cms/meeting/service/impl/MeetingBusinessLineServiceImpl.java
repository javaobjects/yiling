package com.yiling.cms.meeting.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.cms.meeting.dao.MeetingBusinessLineMapper;
import com.yiling.cms.meeting.entity.MeetingBusinessLineDO;
import com.yiling.cms.meeting.service.MeetingBusinessLineService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会议引用业务线表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-02
 */
@Service
public class MeetingBusinessLineServiceImpl extends BaseServiceImpl<MeetingBusinessLineMapper, MeetingBusinessLineDO> implements MeetingBusinessLineService {

    @Override
    public List<MeetingBusinessLineDO> getBusinessLineByMeetingId(Long meetingId) {
        LambdaQueryWrapper<MeetingBusinessLineDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MeetingBusinessLineDO::getMeetingId, meetingId);
        return this.list(wrapper);
    }
}
