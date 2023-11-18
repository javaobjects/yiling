package com.yiling.open.cms.meeting.controller;

import java.util.Date;
import java.util.Objects;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.collect.api.MyCollectApi;
import com.yiling.cms.collect.dto.MyCollectDTO;
import com.yiling.cms.collect.dto.request.QueryCollectRequest;
import com.yiling.cms.collect.enums.CollectStatusEnums;
import com.yiling.cms.common.enums.BusinessLineEnum;
import com.yiling.cms.meeting.api.MeetingApi;
import com.yiling.cms.meeting.dto.MeetingDTO;
import com.yiling.cms.meeting.dto.request.QueryMeetingPageListRequest;
import com.yiling.cms.read.api.MyReadApi;
import com.yiling.cms.read.dto.request.AddMyReadRequest;
import com.yiling.cms.read.enums.ReadTypeEnums;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.DubboUtils;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.open.cms.content.form.GetContentForm;
import com.yiling.open.cms.meeting.form.QueryMeetingPageListForm;
import com.yiling.open.cms.meeting.vo.MeetingListVO;
import com.yiling.open.cms.meeting.vo.MeetingVO;

import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 会议管理Controller
 *
 * @author: hongyang.zhang
 * @data: 2022/06/06
 */
@Api(tags = "会议管理")
@RestController
@RequestMapping("/meeting")
public class MeetingController extends BaseController {

    @DubboReference
    MeetingApi meetingApi;

    @DubboReference(async = true)
    MeetingApi syncMeetingApi;

    @Autowired
    FileService fileService;

    @DubboReference(async = true)
    MyReadApi myReadApi;


    @DubboReference
    MyCollectApi myCollectApi;

    @ApiOperation(value = "会议列表")
    @PostMapping("/pageList")
    public Result<Page<MeetingListVO>> pageListTwoPage(@RequestBody @Valid QueryMeetingPageListForm form) {
        QueryMeetingPageListRequest request = PojoUtils.map(form, QueryMeetingPageListRequest.class);
        if (form.getUseLine() != null) {
            request.setUseLineList(ListUtil.toList(form.getUseLine()));
        }
        Page<MeetingDTO> meetingDTOPage = meetingApi.queryMeetingListPage(request);
        Page<MeetingListVO> voPage = PojoUtils.map(meetingDTOPage, MeetingListVO.class);
        voPage.getRecords().forEach(meetingListVO -> meetingListVO.setBackgroundPic(fileService.getUrl(meetingListVO.getBackgroundPic(), FileTypeEnum.MEETING_BACKGROUND_PIC)));
        return Result.success(voPage);
    }


    @ApiOperation(value = "会议详情")
    @GetMapping("/getMeeting")
    public Result<MeetingVO> getMeeting(@Valid GetContentForm form) {
        MeetingDTO meeting = meetingApi.getMeeting(form.getId());
        MeetingVO vo = PojoUtils.map(meeting, MeetingVO.class);
        vo.setBackgroundPic(fileService.getUrl(vo.getBackgroundPic(), FileTypeEnum.MEETING_BACKGROUND_PIC));
        // 增加阅读量
        syncMeetingApi.increaseRead(form.getId());
        if (Objects.nonNull(form.getWxDoctorId())) {
            //查询收藏状态
            QueryCollectRequest request = new QueryCollectRequest();
            request.setCollectId(form.getId()).setCollectType(ReadTypeEnums.MEETING.getCode()).setSource(BusinessLineEnum.DOCTOR.getCode()).setOpUserId(form.getWxDoctorId());
            MyCollectDTO myCollectDTO = myCollectApi.getOne(request);
            if (Objects.isNull(myCollectDTO) || null == myCollectDTO.getStatus()) {
                vo.setCollectStatus(CollectStatusEnums.UN_COLLECTED.getCode());
            } else {
                vo.setCollectStatus(myCollectDTO.getStatus());
            }
            //我的阅读插入数据
            AddMyReadRequest readRequest = new AddMyReadRequest();
            readRequest.setContentTime(meeting.getCreateTime()).setReadType(ReadTypeEnums.MEETING.getCode()).setSource(BusinessLineEnum.DOCTOR.getCode()).setReadId(meeting.getId()).setTitle(meeting.getTitle()).setOpTime(new Date());
            readRequest.setOpUserId(form.getWxDoctorId());
            myReadApi.save(readRequest);
            DubboUtils.quickAsyncCall("myReadApi", "save");
        }
        return Result.success(vo);
    }

}
