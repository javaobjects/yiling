package com.yiling.sales.assistant.app.mr.meeting.controller;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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
import com.yiling.cms.common.CmsErrorCode;
import com.yiling.cms.meeting.api.MeetingApi;
import com.yiling.cms.meeting.dto.MeetingDTO;
import com.yiling.cms.meeting.dto.request.QueryMeetingPageListRequest;
import com.yiling.cms.meeting.enums.MeetingShowStatusEnum;
import com.yiling.cms.meeting.enums.MeetingStatusEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.sales.assistant.app.mr.meeting.form.QueryMeetingPageListForm;
import com.yiling.sales.assistant.app.mr.meeting.vo.MeetingListItemVO;
import com.yiling.sales.assistant.app.mr.meeting.vo.MeetingVO;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.enums.EmployeeTypeEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 销售助手-医药代表 会议邀请 前端控制器
 * </p>
 *
 * @author lun.yu
 * @date 2022-06-07
 */
@Api(tags = "会议邀请")
@RestController
@RequestMapping("/meeting")
public class MeetingController extends BaseController {

    @DubboReference
    MeetingApi meetingApi;

    @DubboReference
    EmployeeApi employeeApi;

    @Autowired
    FileService fileService;

    @ApiOperation(value = "会议列表")
    @PostMapping("/pageList")
    public Result<Page<MeetingListItemVO>> pageList(@CurrentUser CurrentStaffInfo staffInfo , @RequestBody @Valid QueryMeetingPageListForm form) {
        QueryMeetingPageListRequest request = PojoUtils.map(form, QueryMeetingPageListRequest.class);
        request.setStatus(MeetingStatusEnum.HAVE_RELEASE.getCode());
        request.setActivityStartTimeDesc(true);
        // 医药代表查询使用业务线必须包含医代
        request.setUseLineList(ListUtil.toList(5));
        // 公开状态：不公开仅以岭内部医药代表可查看
        request.setPublicFlag(1);
        if(Constants.YILING_EID.compareTo(staffInfo.getCurrentEid()) == 0){
            EnterpriseEmployeeDTO enterpriseEmployeeDTO = employeeApi.getByEidUserId(Constants.YILING_EID, staffInfo.getCurrentUserId());
            // 以岭内部医药代表才可以看到非公开的会议
            if(Objects.nonNull(enterpriseEmployeeDTO) && enterpriseEmployeeDTO.getType().equals(EmployeeTypeEnum.MR.getCode())){
                request.setPublicFlag(null);
            }
        }

        Page<MeetingDTO> meetingDTOPage = meetingApi.queryMeetingListPage(request);

        meetingDTOPage.getRecords().forEach(this::setStatus);
        Page<MeetingListItemVO> voPage = PojoUtils.map(meetingDTOPage, MeetingListItemVO.class);
        voPage.getRecords().forEach(meetingListItemVO -> meetingListItemVO.setBackgroundPic(fileService.getUrl(meetingListItemVO.getBackgroundPic(), FileTypeEnum.MEETING_BACKGROUND_PIC)));

        return Result.success(voPage);
    }

    @ApiOperation(value = "会议详情")
    @GetMapping("/getMeeting")
    public Result<MeetingVO> getMeeting(@CurrentUser CurrentStaffInfo staffInfo , @RequestParam("id") @ApiParam(value = "会议ID", required = true) Long id) {
        MeetingDTO meetingDTO = meetingApi.getMeeting(id);
        if (meetingDTO.getStatus().equals(MeetingStatusEnum.NOT_RELEASE.getCode())) {
            throw new BusinessException(CmsErrorCode.MEETING_HAVE_CANCEL);
        }
        this.setStatus(meetingDTO);
        meetingDTO.setBackgroundPic(fileService.getUrl(meetingDTO.getBackgroundPic(), FileTypeEnum.MEETING_BACKGROUND_PIC));
        return Result.success(PojoUtils.map(meetingDTO, MeetingVO.class));
    }

    private void setStatus(MeetingDTO meetingDTO) {
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
    }

}
