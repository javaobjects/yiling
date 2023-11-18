package com.yiling.hmc.meeting.api.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.model.ObjectMetadata;
import com.yiling.basic.wx.api.GzhUserApi;
import com.yiling.basic.wx.dto.GzhUserDTO;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.gzh.api.HmcGzhApi;
import com.yiling.hmc.gzh.enums.HmcGzhSubscribeStatusEnum;
import com.yiling.hmc.meeting.api.HmcMeetingSignApi;
import com.yiling.hmc.meeting.dto.MeetingSignDTO;
import com.yiling.hmc.meeting.dto.SubmitMeetingCheckCodeDTO;
import com.yiling.hmc.meeting.dto.SubmitMeetingSignDTO;
import com.yiling.hmc.meeting.dto.request.GetMeetingSignRequest;
import com.yiling.hmc.meeting.dto.request.SubmitCheckCodeRequest;
import com.yiling.hmc.meeting.dto.request.SubmitMeetingSignRequest;
import com.yiling.hmc.meeting.service.MeetingSignService;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.HmcUser;
import com.yiling.user.system.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * @author: fan.shen
 * @date: 2023-04-12
 */
@Slf4j
@DubboService
public class HmcMeetingSignApiImpl implements HmcMeetingSignApi {

    @Autowired
    MeetingSignService meetingSignService;

    @Override
    public void saveMeeting(List<MeetingSignDTO> meetingSignDTOList) {
        meetingSignService.saveMeeting(meetingSignDTOList);
    }

    @Override
    public List<MeetingSignDTO> getMeetingSignInfo(GetMeetingSignRequest request) {
        return meetingSignService.getMeetingSignInfo(request);
    }

    @Override
    public SubmitMeetingSignDTO submitMeetingSign(SubmitMeetingSignRequest request) {
        return meetingSignService.submitMeetingSign(request);
    }

    @Override
    public SubmitMeetingCheckCodeDTO submitCheckCode(SubmitCheckCodeRequest request) {
        return meetingSignService.submitCheckCode(request);
    }
}
