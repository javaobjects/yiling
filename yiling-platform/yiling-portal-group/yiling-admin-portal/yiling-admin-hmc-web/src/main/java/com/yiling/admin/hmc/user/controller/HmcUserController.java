package com.yiling.admin.hmc.user.controller;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import cn.hutool.core.util.StrUtil;
import com.yiling.hmc.activity.api.HMCActivityApi;
import com.yiling.hmc.activity.api.HMCActivityPatientEducateApi;
import com.yiling.hmc.activity.dto.ActivityDTO;
import com.yiling.hmc.activity.dto.ActivityDocToPatientDTO;
import com.yiling.hmc.activity.dto.ActivityPatientEducateDTO;
import com.yiling.hmc.activity.dto.request.QueryActivityRequest;
import com.yiling.hmc.activity.enums.ActivityTypeEnum;
import com.yiling.hmc.wechat.enums.HmcActivitySourceEnum;
import com.yiling.hmc.wechat.enums.SourceEnum;
import com.yiling.ih.patient.api.HmcPatientApi;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.api.IHActivityPatientEducateApi;
import com.yiling.ih.user.dto.DoctorAppInfoDTO;
import com.yiling.ih.user.dto.request.QueryDoctorRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.admin.hmc.user.form.QueryHmcUserPageListForm;
import com.yiling.admin.hmc.user.vo.HmcUserVO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.patient.api.PatientApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.HmcUser;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.request.QueryHmcUserPageListRequest;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 健康中心用户
 *
 * @author: gxl
 * @date: 2022/4/22
 */
@RestController
@Api(tags = "用户管理")
@RequestMapping("user")
@Slf4j
public class HmcUserController extends BaseController {

    @DubboReference
    HmcUserApi hmcUserApi;

    @DubboReference
    UserApi userApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    PatientApi patientApi;

    @DubboReference
    HmcPatientApi hmcPatientApi;

    @DubboReference
    HMCActivityPatientEducateApi hmcActivityPatientEducateApi;

    @DubboReference
    HMCActivityApi hmcActivityApi;

    @DubboReference
    IHActivityPatientEducateApi ihActivityPatientEducateApi;

    @DubboReference
    DoctorApi doctorApi;

    @ApiOperation(value = "注册用户列表", httpMethod = "GET")
    @GetMapping("queryUserPage")
    public Result<Page<HmcUserVO>> queryUserPage(QueryHmcUserPageListForm form) {
        QueryHmcUserPageListRequest request = new QueryHmcUserPageListRequest();
        PojoUtils.map(form, request);
        if (StrUtil.isNotBlank(form.getActivityName()) || Objects.nonNull(form.getActivityId())) {
            QueryActivityRequest build = QueryActivityRequest.builder().build();
            build.setActivityName(form.getActivityName());
            if (Objects.nonNull(form.getActivityId()) && form.getActivityId().compareTo(0L) > 0) {
                build.setActivityIdList(Collections.singletonList(form.getActivityId()));
            }
            // 查询患教活动
            List<ActivityPatientEducateDTO> activityPatientEducateDTOS = hmcActivityPatientEducateApi.queryActivity(build);

            // 查询医带患活动
            List<ActivityDTO> activityDTOList = hmcActivityApi.queryActivity(build);

            if (CollUtil.isEmpty(activityPatientEducateDTOS) && CollUtil.isEmpty(activityDTOList)) {
                return Result.success(form.getPage());
            }
            List<Long> patientEducateIdList = activityPatientEducateDTOS.stream().map(ActivityPatientEducateDTO::getId).collect(Collectors.toList());
            List<Long> activityIdList = activityDTOList.stream().map(ActivityDTO::getId).collect(Collectors.toList());
            patientEducateIdList.addAll(activityIdList);
            request.setActivityIdList(patientEducateIdList);
        }
        if (Objects.nonNull(request.getRegistEndTime())) {
            request.setRegistEndTime(DateUtil.endOfDay(request.getRegistEndTime()));
        }
        if (StrUtil.isNotBlank(form.getDoctorName()) || Objects.nonNull(form.getDoctorId())) {
            QueryDoctorRequest queryDoctor = new QueryDoctorRequest();
            queryDoctor.setDoctorName(form.getDoctorName());
            queryDoctor.setDoctorId(form.getDoctorId());
            List<Long> doctorIdList = doctorApi.queryDoctorByIdAndName(queryDoctor);
            if (CollUtil.isEmpty(doctorIdList)) {
                return Result.success(form.getPage());
            }
            request.setDoctorIdList(doctorIdList);
        }
        Page<HmcUser> hmcUserPage = hmcUserApi.pageList(request);
        List<HmcUser> records = hmcUserPage.getRecords();
        if (CollUtil.isEmpty(records)) {
            return Result.success(form.getPage());
        }
        List<Long> inviteUserIdList = records.stream().map(HmcUser::getInviteUserId).distinct().collect(Collectors.toList());
        List<UserDTO> userDTOS = userApi.listByIds(inviteUserIdList);
        Map<Long, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity(), (x, y) -> x));
        List<HmcUserVO> userVOList = Lists.newArrayListWithExpectedSize(records.size());
//        查询关联就诊人数
        List<Long> userIdList = records.stream().map(HmcUser::getId).distinct().collect(Collectors.toList());
//        Map<Long, Long> map = patientApi.queryPatientCountByUserId(userIdList);
        Map<Long, Long> map = hmcPatientApi.queryPatientByUserIdList(userIdList);

        // 查询所有患教活动
        List<Long> userActivityList = records.stream().filter(item -> SourceEnum.DOCTOR.getType().equals(item.getRegisterSource()) && HmcActivitySourceEnum.PATIENT_EDUCATE.getType().equals(item.getActivitySource())).
                map(HmcUser::getActivityId).distinct().collect(Collectors.toList());
        Map<Long, ActivityPatientEducateDTO> activityMap = Maps.newHashMap();
        if (CollUtil.isNotEmpty(userActivityList)) {
            QueryActivityRequest activityRequest = QueryActivityRequest.builder().activityIdList(userActivityList).build();
            List<ActivityPatientEducateDTO> activityPatientEducateDTOS = hmcActivityPatientEducateApi.queryActivity(activityRequest);
            activityMap.putAll(activityPatientEducateDTOS.stream().collect(Collectors.toMap(ActivityPatientEducateDTO::getId, o -> o, (k1, k2) -> k1)));
        }

        // 查询所有新活动(医带患、八子补肾)
        List<Long> newActivityIdList = records.stream().filter(item -> SourceEnum.DOCTOR.getType().equals(item.getRegisterSource()) && HmcActivitySourceEnum.checkIsNewActivity(item.getActivitySource())).
                map(HmcUser::getActivityId).distinct().collect(Collectors.toList());
        Map<Long, ActivityDocToPatientDTO> newActivityMap = Maps.newHashMap();
        if (CollUtil.isNotEmpty(newActivityIdList)) {
            List<ActivityDocToPatientDTO> newActivityList = hmcActivityApi.queryActivityByIdList(newActivityIdList);
            newActivityMap.putAll(newActivityList.stream().collect(Collectors.toMap(ActivityDocToPatientDTO::getId, o -> o, (k1, k2) -> k1)));
        }


        List<Long> eidList = records.stream().map(HmcUser::getInviteEid).collect(Collectors.toList());
        Map<Long, EnterpriseDTO> enterpriseDTOMap = Maps.newHashMap();
        if (CollUtil.isNotEmpty(eidList)) {
            eidList = eidList.stream().filter(e -> e > 0).distinct().collect(Collectors.toList());
            if (CollUtil.isNotEmpty(eidList)) {
                enterpriseDTOMap = enterpriseApi.getMapByIds(eidList);
            }
        }
        Map<Long, EnterpriseDTO> finalEnterpriseDTOMap = enterpriseDTOMap;
        records.forEach(hmcUser -> {
            HmcUserVO userVO = new HmcUserVO();
            PojoUtils.map(hmcUser, userVO);
            // 判断是否员工、用户推荐
            if (SourceEnum.checkIsStaffOrUserType(hmcUser.getRegisterSource())) {
                UserDTO userDTO = userDTOMap.getOrDefault(hmcUser.getInviteUserId(), null);
                if (Objects.nonNull(userDTO)) {
                    userVO.setInviteUserName(userDTO.getName());
                } else {
                    userVO.setInviteUserName(Constants.SEPARATOR_MIDDLELINE);
                }
            }

            if (hmcUser.getInviteEid() > 0) {
                userVO.setInviteEname(finalEnterpriseDTOMap.get(hmcUser.getInviteEid()).getName());
            }

            // 患教活动 - 1
            if (ActivityTypeEnum.PATIENT_EDUCATE.getCode().equals(hmcUser.getActivitySource()) && hmcUser.getActivityId() > 0) {
                String activityName = Optional.ofNullable(activityMap.get(hmcUser.getActivityId())).map(ActivityPatientEducateDTO::getActivityName).orElse(null);
                userVO.setActivityName(activityName);
            }
            // 医带患 - 2、八子补肾 - 3
            if (ActivityTypeEnum.isDocPatientOrBaZi(hmcUser.getActivitySource()) && hmcUser.getActivityId() > 0) {
                String activityName = Optional.ofNullable(newActivityMap.get(hmcUser.getActivityId())).map(ActivityDocToPatientDTO::getActivityName).orElse(null);
                userVO.setActivityName(activityName);
            }

            // 如果是医生邀请 -> 设置邀请人姓名
            if (SourceEnum.DOCTOR.getType().equals(hmcUser.getRegisterSource())) {
                DoctorAppInfoDTO doctorInfo = ihActivityPatientEducateApi.getDoctorInfoByDoctorId(hmcUser.getInviteUserId().intValue());
                if (Objects.nonNull(doctorInfo)) {
                    userVO.setInviteUserName(doctorInfo.getDoctorName());
                }
            }

            // 处理头像逻辑->如果昵称是HZ开头，则返回默认头像
            String avatarUrl = hmcUser.getAvatarUrl();
            if (StrUtil.isNotBlank(hmcUser.getNickName()) && hmcUser.getNickName().startsWith(com.yiling.user.common.util.Constants.HZ_NICKNAME_PREFIX)) {
                avatarUrl = com.yiling.user.common.util.Constants.HZ_DEFAULT_AVATAR;
            }
            userVO.setAvatarUrl(avatarUrl);

            userVO.setPatientCount(map.getOrDefault(hmcUser.getId(), 0L));
            userVO.setName(hmcUser.getNickName());
            userVOList.add(userVO);
        });
        Page<HmcUserVO> userVOPage = PojoUtils.map(hmcUserPage, HmcUserVO.class);
        userVOPage.setRecords(userVOList);
        return Result.success(userVOPage);
    }

}