package com.yiling.hmc.meeting.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseSimpleDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterprisePageRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.cms.form.GetQaListForm;
import com.yiling.hmc.meeting.api.HmcMeetingSignApi;
import com.yiling.hmc.meeting.dto.MeetingSignDTO;
import com.yiling.hmc.meeting.dto.SubmitMeetingCheckCodeDTO;
import com.yiling.hmc.meeting.dto.SubmitMeetingSignDTO;
import com.yiling.hmc.meeting.dto.request.GetMeetingSignRequest;
import com.yiling.hmc.meeting.dto.request.SubmitCheckCodeRequest;
import com.yiling.hmc.meeting.dto.request.SubmitMeetingSignRequest;
import com.yiling.hmc.meeting.form.GetMeetingSignInfoForm;
import com.yiling.hmc.meeting.form.QueryCrmEnterprisePageForm;
import com.yiling.hmc.meeting.form.SubmitCheckForm;
import com.yiling.hmc.meeting.form.SubmitSignInfoForm;
import com.yiling.hmc.meeting.vo.CrmEnterpriseVO;
import com.yiling.hmc.meeting.vo.MeetingSignVO;
import com.yiling.hmc.meeting.vo.SubmitCheckCodeVO;
import com.yiling.hmc.meeting.vo.SubmitMeetingSignVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.bo.CurrentUserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 会议签到
 *
 * @author: fan.shen
 * @date: 2023/4/12
 */
@Api(tags = "会议签到")
@RestController
@RequestMapping("/meetingSign")
@Slf4j
public class MeetingSignController extends BaseController {

    @DubboReference
    HmcMeetingSignApi meetingSignApi;

    @DubboReference(timeout = 80 * 1000)
    CrmEnterpriseApi crmEnterpriseApi;

    @ApiOperation("01、获取签到信息")
    @PostMapping("getMeetingInfoByName")
    @Log(title = "获取签到信息", businessType = BusinessTypeEnum.OTHER)
    public Result<List<MeetingSignVO>> getMeetingInfoByName(@RequestBody @Valid GetMeetingSignInfoForm form) {
        GetMeetingSignRequest request = PojoUtils.map(form, GetMeetingSignRequest.class);
        List<MeetingSignDTO> meetingList = meetingSignApi.getMeetingSignInfo(request);
        return Result.success(PojoUtils.map(meetingList, MeetingSignVO.class));
    }

    @ApiOperation("02、提交签到信息")
    @PostMapping("submitMeetingSign")
    @Log(title = "提交签到信息", businessType = BusinessTypeEnum.OTHER)
    public Result<SubmitMeetingSignVO> submitMeetingSign(@RequestBody @Valid SubmitSignInfoForm form) {
        SubmitMeetingSignRequest request = PojoUtils.map(form, SubmitMeetingSignRequest.class);
        SubmitMeetingSignDTO signDTO = meetingSignApi.submitMeetingSign(request);
        SubmitMeetingSignVO signVO = PojoUtils.map(signDTO, SubmitMeetingSignVO.class);
        return Result.success(signVO);
    }

    @ApiOperation("03、提交核销")
    @PostMapping("submitCheckCode")
    @Log(title = "提交核销", businessType = BusinessTypeEnum.OTHER)
    public Result<SubmitCheckCodeVO> submitCheckCode(@RequestBody @Valid SubmitCheckForm form) {
        SubmitCheckCodeRequest request = PojoUtils.map(form, SubmitCheckCodeRequest.class);
        SubmitMeetingCheckCodeDTO checkResult = meetingSignApi.submitCheckCode(request);
        SubmitCheckCodeVO checkCodeVO = PojoUtils.map(checkResult, SubmitCheckCodeVO.class);
        return Result.success(checkCodeVO);
    }


    @ApiOperation(value = "04、获取出库终端/出库商业")
    @PostMapping("/get/crm/enterprise")
    public Result<Page<CrmEnterpriseVO>> getGBCrmEnterprisePage(@RequestBody @Valid QueryCrmEnterprisePageForm form) {
        QueryCrmEnterprisePageRequest request = PojoUtils.map(form, QueryCrmEnterprisePageRequest.class);
        Page<CrmEnterpriseSimpleDTO> crmEnterpriseSimplePage = crmEnterpriseApi.getCrmEnterpriseSimplePage(request);
        return Result.success(PojoUtils.map(crmEnterpriseSimplePage, CrmEnterpriseVO.class));
    }


}