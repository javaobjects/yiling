package com.yiling.hmc.meeting.service;

import com.yiling.hmc.meeting.dto.MeetingSignDTO;
import com.yiling.hmc.meeting.dto.SubmitMeetingCheckCodeDTO;
import com.yiling.hmc.meeting.dto.SubmitMeetingSignDTO;
import com.yiling.hmc.meeting.dto.request.GetMeetingSignRequest;
import com.yiling.hmc.meeting.dto.request.SubmitCheckCodeRequest;
import com.yiling.hmc.meeting.dto.request.SubmitMeetingSignRequest;
import com.yiling.hmc.meeting.entity.MeetingSignDO;
import com.yiling.framework.common.base.BaseService;

import java.util.List;

/**
 * <p>
 * 会议签到 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2023-04-12
 */
public interface MeetingSignService extends BaseService<MeetingSignDO> {

    /**
     * 获取会议签到信息
     * @param request
     * @return
     */
    List<MeetingSignDTO> getMeetingSignInfo(GetMeetingSignRequest request);

    /**
     * 保存
     * @param meetingSignDTOList
     */
    void saveMeeting(List<MeetingSignDTO> meetingSignDTOList);

    /**
     * 提交
     * @param request
     * @return
     */
    SubmitMeetingSignDTO submitMeetingSign(SubmitMeetingSignRequest request);

    /**
     * 提交核销
     * @param request
     * @return
     */
    SubmitMeetingCheckCodeDTO submitCheckCode(SubmitCheckCodeRequest request);
}
