package com.yiling.admin.hmc.activity.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReferenceUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.hmc.activity.form.*;
import com.yiling.admin.hmc.activity.vo.*;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.activity.api.HMCActivityApi;
import com.yiling.hmc.activity.api.HMCActivityPatientEducateApi;
import com.yiling.hmc.activity.dto.ActivityDTO;
import com.yiling.hmc.activity.dto.ActivityDocToPatientDTO;
import com.yiling.hmc.activity.dto.request.BaseActivityRequest;
import com.yiling.hmc.activity.dto.request.QueryActivityRequest;
import com.yiling.hmc.activity.dto.request.SaveActivityDocPatientRequest;
import com.yiling.hmc.activity.enums.ActivityProgressEnum;
import com.yiling.hmc.activity.enums.ActivityTypeEnum;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.api.IHActivityPatientEducateApi;
import com.yiling.ih.user.dto.*;
import com.yiling.ih.user.dto.request.*;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * C端活动 Controller
 *
 * @author: fan.shen
 * @date: 2023-01-13
 */
@RestController
@RequestMapping("/activity")
@Api(tags = "C端活动")
@Slf4j
public class ActivityController extends BaseController {

    @DubboReference
    HMCActivityApi hmcActivityApi;

    @DubboReference
    HMCActivityPatientEducateApi hmcPatientEducateApi;

    @DubboReference
    IHActivityPatientEducateApi ihActivityPatientEducateApi;

    @DubboReference
    DoctorApi doctorApi;

    @Autowired
    private FileService fileService;

    @Autowired
    private WxMpService mpService;

    @DubboReference
    UserApi userApi;

    @ApiOperation(value = "医带患活动列表")
    @PostMapping("/docToPatientPageList")
    @Log(title = "医带患活动列表", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<ActivityDocToPatientVO>> docToPatientPageList(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody QueryActivityForm form) {
        QueryActivityRequest request = PojoUtils.map(form, QueryActivityRequest.class);
        request.setActivityType(ActivityTypeEnum.DOC_TO_PATIENT.getCode());
        Page<ActivityDTO> page = hmcActivityApi.pageList(request);
        if(Objects.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
            return Result.success(form.getPage());
        }
        List<Long> activityIdList = page.getRecords().stream().map(ActivityDTO::getId).collect(Collectors.toList());
        GetActivityDoctorPatientCountRequest countRequest = new GetActivityDoctorPatientCountRequest();
        countRequest.setActivityIds(activityIdList);
        List<ActivityDocPatientCountDTO> countList = doctorApi.getActivityDoctorPatientCount(countRequest);
        Map<Long, ActivityDocPatientCountDTO> countMap = countList.stream().collect(Collectors.toMap(ActivityDocPatientCountDTO::getActivityId, Function.identity()));
        Page<ActivityDocToPatientVO> result = PojoUtils.map(page, ActivityDocToPatientVO.class);
        result.getRecords().forEach(item -> {
            if (DateUtil.compare(item.getEndTime(), DateUtil.date()) < 0) {
                item.setActivityProgress(ActivityProgressEnum.ENDED.getCode());
            } else if (DateUtil.compare(item.getBeginTime(), DateUtil.date()) < 0 && DateUtil.compare(item.getEndTime(), DateUtil.date()) > 0) {
                item.setActivityProgress(ActivityProgressEnum.PROCESSING.getCode());
            } else {
                item.setActivityProgress(ActivityProgressEnum.UN_START.getCode());
            }
            // 停用之后 -> 活动状态变为已结束
            if(item.getActivityStatus()==2) {
                item.setActivityProgress(ActivityProgressEnum.ENDED.getCode());
            }

            if (countMap.containsKey(item.getId())) {
                Long doctorCount = countMap.get(item.getId()).getDoctorCount();
                Long patientCount = countMap.get(item.getId()).getPatientCount();
                Long uvCount = countMap.get(item.getId()).getUvCount();
                item.setActivityDoctorCount(doctorCount);
                item.setPatientCount(patientCount);
                item.setUvCount(uvCount);

            }
            // 设置活动链接
            item.setActivityLink("/view-doctor2/invite-patient-active/index?activeId=" + item.getId());
        });
        return Result.success(result);
    }

    @ApiOperation(value = "保存医带患活动")
    @PostMapping("/saveOrUpdateDocToPatient")
    @Log(title = "保存患教活动", businessType = BusinessTypeEnum.INSERT)
    public Result<Long> saveOrUpdateDocToPatient(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveActivityDocPatientForm form) {
        SaveActivityDocPatientRequest request = PojoUtils.map(form, SaveActivityDocPatientRequest.class);
        request.setActivityStatus(1);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setActivityType(ActivityTypeEnum.DOC_TO_PATIENT.getCode());
        Long id = hmcActivityApi.saveOrUpdateDocToPatient(request);
        return Result.success(id);
    }

    @ApiOperation(value = "医带患活动详情")
    @PostMapping("/queryActivityById")
    @Log(title = "删除患教活动", businessType = BusinessTypeEnum.OTHER)
    public Result<HmcActivityDoctorToPatientDetailVO> queryActivityById(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid BaseActivityForm form) {
        ActivityDocToPatientDTO activity = hmcActivityApi.queryActivityById(form.getId());
        HmcActivityDoctorToPatientDetailVO doctorToPatientDetailVO = PojoUtils.map(activity, HmcActivityDoctorToPatientDetailVO.class);
        if (StrUtil.isNotBlank(activity.getActivityHeadPic())) {
            String url = fileService.getUrl(activity.getActivityHeadPic(), FileTypeEnum.HMC_ACTIVITY_PIC);
            doctorToPatientDetailVO.setActivityHeadPicUrl(url);
        }
        return Result.success(doctorToPatientDetailVO);
    }

    @ApiOperation(value = "停用活动")
    @PostMapping("/stopActivity")
    @Log(title = "停用活动", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> stopActivity(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid BaseActivityForm form) {
        BaseActivityRequest request = new BaseActivityRequest();
        request.setId(form.getId());
        request.setOpUserId(adminInfo.getCurrentUserId());
        Boolean result = hmcActivityApi.stopActivity(request);
        return Result.success(result);
    }

    @ApiOperation(value = "切换医生活动资格")
    @PostMapping("switchActivityDoctorQuality")
    public Result<Boolean> switchActivityDoctorQuality(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid DeleteActivityDoctorForm form) {
        DeleteActivityDoctorRequest request = new DeleteActivityDoctorRequest();
        PojoUtils.map(form, request);
        doctorApi.updateActivityDoctorStatus(request);
        return Result.success(true);
    }


    @ApiOperation(value = "保存活动医生")
    @PostMapping("save")
    public Result<Boolean> save(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveActivityDoctorForm form) {
        SaveActivityDoctorRequest request = new SaveActivityDoctorRequest();
        request.setCreateUser(adminInfo.getCurrentUserId().intValue());

        PojoUtils.map(form, request);

        try {
            List<HmcActivityDoctorQrcodeUrlQuest> hmcActivityDoctorQrcodeUrlFormList = request.getHmcActivityDoctorQrcodeUrlFormList();
            for (HmcActivityDoctorQrcodeUrlQuest doctor : hmcActivityDoctorQrcodeUrlFormList) {
                String sceneStr = "qt:40_actId:%s_docId:%s";
                String qrCode = String.format(sceneStr, form.getActivityId(), doctor.getDoctorId());
                WxMpQrCodeTicket ticket = mpService.getQrcodeService().qrCodeCreateLastTicket(qrCode);
                String url = this.mpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());
                doctor.setQrcodeUrl(url);
            }

        } catch (Exception e) {
            log.error("生成活动码报错,{}", e.getMessage(), e);
            return Result.failed("生成活动码失败");
        }
        doctorApi.saveActivityDocPatientDoctor(request);
        return Result.success(true);
    }

    @ApiOperation(value = "医带患活动医生列表")
    @PostMapping("queryActivityDocPage")
    public Result<Page<HmcActivityDocVO>> queryActivityDocPage(@RequestBody @Valid QueryActivityDoctorPageForm form) {
        QueryActivityDoctorListRequest request = new QueryActivityDoctorListRequest();
        PojoUtils.map(form, request);
        Page<HmcActivityDocDTO> hmcActivityDoctorDTOPage = doctorApi.queryActivityDocPage(request);
        if (CollUtil.isEmpty(hmcActivityDoctorDTOPage.getRecords())) {
            return Result.success(form.getPage());
        }
        Page<HmcActivityDocVO> hmcActivityDoctorVOPage = PojoUtils.map(hmcActivityDoctorDTOPage, HmcActivityDocVO.class);
        return Result.success(hmcActivityDoctorVOPage);
    }

    @ApiOperation(value = "医带患活动患者列表")
    @PostMapping("queryActivityDocPatientPage")
    public Result<Page<HmcActivityDocPatientVO>> queryActivityDocPatientPage(@RequestBody @Valid QueryActivityDocPatientPageForm form) {
        QueryActivityDocPatientListRequest request = new QueryActivityDocPatientListRequest();
        PojoUtils.map(form, request);
        Page<HmcActivityDocPatientDTO> hmcActivityDocPatientDTOPage = doctorApi.queryActivityDocPatientPage(request);
        if (CollUtil.isEmpty(hmcActivityDocPatientDTOPage.getRecords())) {
            return Result.success(form.getPage());
        }
        Page<HmcActivityDocPatientVO> hmcActivityDocPatientVOPage = PojoUtils.map(hmcActivityDocPatientDTOPage, HmcActivityDocPatientVO.class);
        return Result.success(hmcActivityDocPatientVOPage);
    }

    @ApiOperation(value = "医带患活动患者详情")
    @PostMapping("queryActivityDocPatientDetail")
    public Result<HmcActivityDocPatientDetailVO> queryActivityDocPatientDetail(@RequestBody @Valid QueryActivityDocPatientDetailForm form) {
        HmcActivityDocPatientDetailDTO patientDetailDTO = doctorApi.queryActivityDocPatientDetail(form.getId());

        HmcActivityDocPatientDetailVO detailVO = PojoUtils.map(patientDetailDTO, HmcActivityDocPatientDetailVO.class);

        if (CollUtil.isNotEmpty(patientDetailDTO.getLogList())) {
            List<Long> userIdList = patientDetailDTO.getLogList().stream().map(item -> Long.valueOf(item.getCreateUser())).collect(Collectors.toList());
            List<UserDTO> userDTOList = userApi.listByIds(userIdList);
            Map<Long, UserDTO> userMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, o -> o, (k1, k2) -> k1));
            detailVO.getLogList().forEach(item -> {
                String createUserName = Optional.ofNullable(userMap.get(Long.valueOf(item.getCreateUser()))).map(UserDTO::getName).orElse(null);
                item.setCreateUserName(createUserName);
            });
        }

        return Result.success(detailVO);
    }

    @ApiOperation(value = "医带患活动患者审核")
    @PostMapping("activityDocPatientAudit")
    public Result<Boolean> activityDocPatientAudit(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid ActivityDocPatientAuditForm form) {
        ActivityDocPatientAuditRequest request = PojoUtils.map(form, ActivityDocPatientAuditRequest.class);
        request.setCreateUser(adminInfo.getCurrentUserId().intValue());
        return Result.success(doctorApi.activityDocPatientAudit(request));
    }

}
