package com.yiling.admin.hmc.activity.controller;

import javax.validation.Valid;

import cn.hutool.core.collection.CollUtil;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.api.IHActivityPatientEducateApi;
import com.yiling.ih.user.dto.HmcActivityDoctorDTO;
import com.yiling.ih.user.dto.request.QueryActivityDoctorListRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.hmc.activity.form.QueryActivityForm;
import com.yiling.admin.hmc.activity.form.BaseActivityForm;
import com.yiling.admin.hmc.activity.form.SaveActivityForm;
import com.yiling.admin.hmc.activity.vo.ActivityPatientEducateVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.activity.api.HMCActivityPatientEducateApi;
import com.yiling.hmc.activity.dto.ActivityPatientEducateDTO;
import com.yiling.hmc.activity.dto.request.QueryActivityPatientEducateRequest;
import com.yiling.hmc.activity.dto.request.SaveActivityPatientEducationRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 患教活动 Controller
 *
 * @author: fan.shen
 * @date: 2022/9/2
 */
@RestController
@RequestMapping("/activityPatientEducate")
@Api(tags = "患教活动")
@Slf4j
public class ActivityPatientEducateController extends BaseController {

    @DubboReference
    HMCActivityPatientEducateApi hmcPatientEducateApi;

    @DubboReference
    IHActivityPatientEducateApi ihActivityPatientEducateApi;

    @DubboReference
    DoctorApi doctorApi;

    @ApiOperation(value = "患教活动列表")
    @PostMapping("/pageList")
    @Log(title = "患教活动列表", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<ActivityPatientEducateVO>> pageList(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody QueryActivityForm form) {
        QueryActivityPatientEducateRequest request = PojoUtils.map(form, QueryActivityPatientEducateRequest.class);
        Page<ActivityPatientEducateDTO> page = hmcPatientEducateApi.pageList(request);
        List<Integer> activityIdList = page.getRecords().stream().map(item -> item.getId().intValue()).collect(Collectors.toList());
        Map<Integer, Long> activityDoctorCount = ihActivityPatientEducateApi.queryActivityDoctorCount(activityIdList);
        page.getRecords().forEach(item -> item.setActivityDoctorCount(activityDoctorCount.getOrDefault(item.getId().intValue(), 0L)));
        Page<ActivityPatientEducateVO> result = PojoUtils.map(page, ActivityPatientEducateVO.class);
        return Result.success(result);
    }

    @ApiOperation(value = "保存患教活动")
    @PostMapping("/save")
    @Log(title = "保存患教活动", businessType = BusinessTypeEnum.INSERT)
    public Result<ActivityPatientEducateVO> saveOrUpdate(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveActivityForm form) {
        SaveActivityPatientEducationRequest request = PojoUtils.map(form, SaveActivityPatientEducationRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        ActivityPatientEducateDTO patientEducateDTO = hmcPatientEducateApi.saveActivityPatientEducate(request);
        return Result.success(PojoUtils.map(patientEducateDTO, ActivityPatientEducateVO.class));
    }

    @ApiOperation(value = "患教活动详情")
    @PostMapping("/queryActivityById")
    @Log(title = "删除患教活动", businessType = BusinessTypeEnum.OTHER)
    public Result<ActivityPatientEducateVO> queryActivityById(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid BaseActivityForm form) {
        ActivityPatientEducateDTO patientEducateDTO = hmcPatientEducateApi.queryActivityById(form.getId());
        return Result.success(PojoUtils.map(patientEducateDTO, ActivityPatientEducateVO.class));
    }

    @ApiOperation(value = "删除患教活动")
    @PostMapping("/delActivityById")
    @Log(title = "删除患教活动", businessType = BusinessTypeEnum.DELETE)
    public Result<Boolean> delActivityById(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid BaseActivityForm form) {
        QueryActivityDoctorListRequest request = new QueryActivityDoctorListRequest();
        request.setActivityId(form.getId().intValue());
        Page<HmcActivityDoctorDTO> activityDoctorPage = doctorApi.queryActivityDoctorList(request);
        if (Objects.nonNull(activityDoctorPage) && CollUtil.isNotEmpty(activityDoctorPage.getRecords())) {
            return Result.failed("已经关联活动医生，禁止删除");
        }
        boolean result = hmcPatientEducateApi.delActivityById(form.getId());
        return Result.success(result);
    }

}
