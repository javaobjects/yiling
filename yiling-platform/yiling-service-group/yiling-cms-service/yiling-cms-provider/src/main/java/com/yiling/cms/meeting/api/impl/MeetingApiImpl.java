package com.yiling.cms.meeting.api.impl;


import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.meeting.api.MeetingApi;
import com.yiling.cms.meeting.dto.MeetingDTO;
import com.yiling.cms.meeting.dto.request.QueryMeetingListRequest;
import com.yiling.cms.meeting.dto.request.QueryMeetingPageListRequest;
import com.yiling.cms.meeting.dto.request.SaveMeetingRequest;
import com.yiling.cms.meeting.dto.request.UpdateMeetingStatusRequest;
import com.yiling.cms.meeting.service.MeetingService;

import lombok.extern.slf4j.Slf4j;

/**
 * 会议管理 API 实现
 *
 * @author: lun.yu
 * @date: 2022-06-01
 */
@Slf4j
@DubboService
public class MeetingApiImpl implements MeetingApi {

    @Autowired
    private MeetingService meetingService;

    @Override
    public Page<MeetingDTO> queryMeetingListPage(QueryMeetingPageListRequest request) {
        return meetingService.queryMeetingListPage(request);
    }

    @Override
    public List<MeetingDTO> queryMeetingList(QueryMeetingListRequest request) {
        return meetingService.queryMeetingList(request);
    }

    @Override
    public MeetingDTO getMeeting(Long id) {
        return meetingService.getMeeting(id);
    }

    @Override
    public boolean saveMeeting(SaveMeetingRequest request) {
        return meetingService.saveMeeting(request);
    }


    @Override
    public boolean deleteMeeting(Long id, Long opUserId) {
        return meetingService.deleteMeeting(id, opUserId);
    }

    @Override
    public boolean increaseRead(Long id) {
        return meetingService.increaseRead(id);
    }

    @Override
    public boolean updateMeetingStatus(UpdateMeetingStatusRequest request) {
        return meetingService.updateMeetingStatus(request);
    }
}
