package com.yiling.hmc.meeting.api;


import com.yiling.hmc.meeting.dto.MeetingSignDTO;
import com.yiling.hmc.meeting.dto.SubmitMeetingCheckCodeDTO;
import com.yiling.hmc.meeting.dto.SubmitMeetingSignDTO;
import com.yiling.hmc.meeting.dto.request.GetMeetingSignRequest;
import com.yiling.hmc.meeting.dto.request.SubmitCheckCodeRequest;
import com.yiling.hmc.meeting.dto.request.SubmitMeetingSignRequest;

import java.util.List;

/**
 * HMC 会议签到 API
 *
 * @Author fan.shen
 * @Date 2023-04-12
 */
public interface HmcMeetingSignApi {

    void saveMeeting(List<MeetingSignDTO> meetingSignDTOList);

    /**
     * 获取会议签到信息
     * @param request
     * @return
     */
    List<MeetingSignDTO> getMeetingSignInfo(GetMeetingSignRequest request);

    /**
     * 提交签到
     *
     * @param request
     * @return
     */
    SubmitMeetingSignDTO submitMeetingSign(SubmitMeetingSignRequest request);

    /**
     * 核销
     * @param request
     * @return
     */
    SubmitMeetingCheckCodeDTO submitCheckCode(SubmitCheckCodeRequest request);
}
