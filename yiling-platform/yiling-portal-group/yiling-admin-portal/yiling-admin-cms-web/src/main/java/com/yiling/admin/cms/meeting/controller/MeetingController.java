package com.yiling.admin.cms.meeting.controller;

import java.util.Date;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.cms.meeting.form.QueryMeetingPageListForm;
import com.yiling.admin.cms.meeting.form.SaveMeetingForm;
import com.yiling.admin.cms.meeting.form.UpdateMeetingStatusForm;
import com.yiling.admin.cms.meeting.vo.MeetingListItemVO;
import com.yiling.admin.cms.meeting.vo.MeetingVO;
import com.yiling.cms.meeting.api.MeetingApi;
import com.yiling.cms.meeting.dto.MeetingDTO;
import com.yiling.cms.meeting.dto.request.QueryMeetingPageListRequest;
import com.yiling.cms.meeting.dto.request.SaveMeetingRequest;
import com.yiling.cms.meeting.dto.request.UpdateMeetingStatusRequest;
import com.yiling.cms.meeting.enums.MeetingShowStatusEnum;
import com.yiling.cms.meeting.enums.MeetingStatusEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 会议管理表 前端控制器
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-02
 */
@Api(tags = "会议管理")
@RestController
@RequestMapping("/meeting")
public class MeetingController extends BaseController {

    @DubboReference
    MeetingApi meetingApi;
    @Autowired
    FileService fileService;

    @ApiOperation(value = "会议分页列表")
    @PostMapping("/pageList")
    public Result<Page<MeetingListItemVO>> pageList(@RequestBody @Valid QueryMeetingPageListForm form) {
        QueryMeetingPageListRequest request = PojoUtils.map(form, QueryMeetingPageListRequest.class);
        if (form.getUseLine() != null && form.getUseLine() != 0) {
            request.setUseLineList(ListUtil.toList(form.getUseLine()));
        }
        Page<MeetingDTO> meetingDTOPage = meetingApi.queryMeetingListPage(request);

        meetingDTOPage.getRecords().forEach(meetingDTO -> {
            Date now = new Date();
            Date activityStartTime = meetingDTO.getActivityStartTime();
            Date activityEndTime = meetingDTO.getActivityEndTime();

            if (meetingDTO.getStatus().equals(MeetingStatusEnum.NOT_RELEASE.getCode())) {
                // 未发布：活动时间在当前时间之前就算未发布已过期，在当前时间之后就是未发布
                if (activityStartTime.before(now)) {
                    meetingDTO.setStatus(MeetingShowStatusEnum.NOT_RELEASE_EXPIRED.getCode());
                } else {
                    meetingDTO.setStatus(MeetingShowStatusEnum.NOT_RELEASE.getCode());
                }
            } else {
                // 已发布：2-进行中 3-已结束 4-未开始
                if (now.after(activityStartTime) && now.before(activityEndTime)) {
                    meetingDTO.setStatus(MeetingShowStatusEnum.DOING.getCode());
                } else if (now.after(activityEndTime)) {
                    meetingDTO.setStatus(MeetingShowStatusEnum.FINISHED.getCode());
                } else if (now.before(activityStartTime)) {
                    meetingDTO.setStatus(MeetingShowStatusEnum.NOT_BEGIN.getCode());
                }
            }
            meetingDTO.setBackgroundPic(fileService.getUrl(meetingDTO.getBackgroundPic(), FileTypeEnum.MEETING_BACKGROUND_PIC));
        });
        Page<MeetingListItemVO> voPage = PojoUtils.map(meetingDTOPage, MeetingListItemVO.class);

        return Result.success(voPage);
    }

    @ApiOperation(value = "获取单个会议")
    @GetMapping("/getMeeting")
    public Result<MeetingVO> getMeeting(@CurrentUser CurrentAdminInfo adminInfo , @RequestParam("id") @ApiParam(value = "会议ID", required = true) Long id) {
        MeetingDTO meetingDTO = meetingApi.getMeeting(id);
        MeetingVO meetingVO = PojoUtils.map(meetingDTO, MeetingVO.class);
        meetingVO.setBackgroundPicUrl(fileService.getUrl(meetingVO.getBackgroundPic(), FileTypeEnum.MEETING_BACKGROUND_PIC));
        return Result.success(meetingVO);
    }

    @ApiOperation(value = "新增或修改会议", notes = "有ID表示为修改，没有ID则为新增")
    @PostMapping("/saveMeeting")
    public Result<Void> saveMeeting(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody @Valid SaveMeetingForm form) {
        SaveMeetingRequest request = PojoUtils.map(form, SaveMeetingRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        meetingApi.saveMeeting(request);

        return Result.success();
    }

    @ApiOperation(value = "发布/取消发布/删除")
    @PostMapping("/updateMeetingStatus")
    public Result<Void> updateMeetingStatus(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody @Valid UpdateMeetingStatusForm form) {
        UpdateMeetingStatusRequest request = PojoUtils.map(form, UpdateMeetingStatusRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        meetingApi.updateMeetingStatus(request);

        return Result.success();
    }


}
