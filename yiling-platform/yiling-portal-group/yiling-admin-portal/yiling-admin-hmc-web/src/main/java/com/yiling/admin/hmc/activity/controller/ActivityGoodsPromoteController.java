package com.yiling.admin.hmc.activity.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
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
import com.yiling.hmc.activity.dto.request.SaveActivityGoodsPromoteRequest;
import com.yiling.hmc.activity.enums.ActivityProgressEnum;
import com.yiling.hmc.activity.enums.ActivityTypeEnum;
import com.yiling.hmc.wechat.enums.HmcActivitySourceEnum;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.api.IHActivityPatientEducateApi;
import com.yiling.ih.user.dto.*;
import com.yiling.ih.user.dto.request.*;
import com.yiling.user.system.api.HmcUserApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentAdminInfo;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.request.QueryActivityDoctorUserCountRequest;
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
 * C端商品推广活动 Controller
 *
 * @author: fan.shen
 * @date: 2023-02-13
 */
@RestController
@RequestMapping("/activity/goodsPromote/")
@Api(tags = "C端商品推广活动")
@Slf4j
public class ActivityGoodsPromoteController extends BaseController {

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

    @DubboReference
    HmcUserApi hmcUserApi;

    @ApiOperation(value = "商品推广活动列表")
    @PostMapping("/PageList")
    @Log(title = "商品推广活动列表", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<ActivityGoodsPromoteVO>> docToPatientPageList(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody QueryActivityForm form) {
        QueryActivityRequest request = PojoUtils.map(form, QueryActivityRequest.class);
        // 查询八子补肾活动
        request.setActivityType(ActivityTypeEnum.BA_ZI_BU_SHEN.getCode());
        Page<ActivityDTO> page = hmcActivityApi.pageList(request);
        if (Objects.isNull(page) || CollUtil.isEmpty(page.getRecords())) {
            return Result.success(form.getPage());
        }
        Page<ActivityGoodsPromoteVO> result = PojoUtils.map(page, ActivityGoodsPromoteVO.class);
        List<Integer> activityIdList = result.getRecords().stream().map(item -> item.getId().intValue()).collect(Collectors.toList());
        List<BaZiActivityDoctorCountDTO> baZiActivityDoctorCountDTOS = ihActivityPatientEducateApi.queryBaZiActivityDoctorCount(activityIdList);
        if (CollUtil.isEmpty(baZiActivityDoctorCountDTOS)) {
            return Result.success(result);
        }
        Map<Integer, BaZiActivityDoctorCountDTO> activityDoctorCountDTOMap = baZiActivityDoctorCountDTOS.stream().collect(Collectors.toMap(BaZiActivityDoctorCountDTO::getActivityId, Function.identity()));
        result.getRecords().forEach(item -> {
            if (activityDoctorCountDTOMap.containsKey(item.getId().intValue())) {
                item.setActivityDoctorCount(activityDoctorCountDTOMap.get(item.getId().intValue()).getDoctorCount());
            }
        });
        return Result.success(result);
    }

    @ApiOperation(value = "保存商品推广活动")
    @PostMapping("/saveOrUpdateGoodsPromote")
    @Log(title = "保存商品推广活动", businessType = BusinessTypeEnum.INSERT)
    public Result<Long> saveOrUpdateGoodsPromote(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveActivityForm form) {
        SaveActivityGoodsPromoteRequest request = PojoUtils.map(form, SaveActivityGoodsPromoteRequest.class);
        request.setActivityStatus(1);
        request.setOpUserId(adminInfo.getCurrentUserId());
        request.setActivityType(ActivityTypeEnum.BA_ZI_BU_SHEN.getCode());
        Long id = hmcActivityApi.saveOrUpdateGoodsPromote(request);
        return Result.success(id);
    }

    @ApiOperation(value = "商品推广活动详情")
    @PostMapping("/queryActivityById")
    @Log(title = "商品推广活动详情", businessType = BusinessTypeEnum.OTHER)
    public Result<ActivityGoodsPromoteVO> queryActivityById(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid BaseActivityForm form) {
        ActivityDTO activity = hmcActivityApi.queryActivityGoodsPromoteById(form.getId());
        ActivityGoodsPromoteVO goodsPromoteVO = PojoUtils.map(activity, ActivityGoodsPromoteVO.class);
        return Result.success(goodsPromoteVO);
    }

    @ApiOperation(value = "删除活动")
    @PostMapping("/delActivity")
    @Log(title = "停用活动", businessType = BusinessTypeEnum.OTHER)
    public Result<Boolean> delActivity(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid BaseActivityForm form) {

        QueryActivityDoctorListRequest queryDoctorRequest = PojoUtils.map(form, QueryActivityDoctorListRequest.class);
        queryDoctorRequest.setActivityId(form.getId().intValue());

        Page<HmcActivityDocDTO> hmcActivityDoctorDTOPage = doctorApi.queryActivityBaZiPage(queryDoctorRequest);
        if (Objects.nonNull(hmcActivityDoctorDTOPage) && CollUtil.isNotEmpty(hmcActivityDoctorDTOPage.getRecords())) {
            return Result.failed("已经关联活动医生，禁止删除");
        }

        BaseActivityRequest request = new BaseActivityRequest();
        request.setId(form.getId());
        request.setOpUserId(adminInfo.getCurrentUserId());
        Boolean result = hmcActivityApi.delActivity(request);
        return Result.success(result);
    }

    @ApiOperation(value = "删除活动医生")
    @PostMapping("delActivityDoctor")
    public Result<Boolean> delActivityDoctor(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid DeleteActivityDoctorForm form) {
        DeleteActivityDoctorRequest request = new DeleteActivityDoctorRequest();
        PojoUtils.map(form, request);
        doctorApi.deleteActivityBaZiDoctorStatus(request);
        return Result.success(true);
    }

    @ApiOperation(value = "保存活动医生")
    @PostMapping("save")
    public Result<Boolean> save(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveActivityDoctorForm form) {
        SaveActivityDoctorRequest request = PojoUtils.map(form, SaveActivityDoctorRequest.class);
        request.setCreateUser(adminInfo.getCurrentUserId().intValue());
        try {
            List<HmcActivityDoctorQrcodeUrlQuest> hmcActivityDoctorQrcodeUrlFormList = request.getHmcActivityDoctorQrcodeUrlFormList();
            for (HmcActivityDoctorQrcodeUrlQuest doctor : hmcActivityDoctorQrcodeUrlFormList) {
                String goodsPromoteQrCodeUrl = hmcActivityApi.getGoodsPromoteQrCodeUrl(form.getActivityId(), doctor.getDoctorId());
                doctor.setQrcodeUrl(goodsPromoteQrCodeUrl);
            }
        } catch (Exception e) {
            log.error("生成商品推广活动码报错,{}", e.getMessage(), e);
            return Result.failed("生成商品推广码失败");
        }
        log.info("保存商品推广活动医生参数：{}", JSONUtil.toJsonStr(request));
        doctorApi.saveActivityBaZiDoctor(request);
        return Result.success(true);
    }

    @ApiOperation(value = "商品推广活动医生列表")
    @PostMapping("queryActivityDocPage")
    public Result<Page<HmcActivityDocVO>> queryActivityDocPage(@RequestBody @Valid QueryActivityDoctorPageForm form) {
        QueryActivityDoctorListRequest request = new QueryActivityDoctorListRequest();
        PojoUtils.map(form, request);
        Page<HmcActivityDocDTO> hmcActivityDoctorDTOPage = doctorApi.queryActivityBaZiPage(request);
        if (CollUtil.isEmpty(hmcActivityDoctorDTOPage.getRecords())) {
            return Result.success(form.getPage());
        }
        Page<HmcActivityDocVO> hmcActivityDoctorVOPage = PojoUtils.map(hmcActivityDoctorDTOPage, HmcActivityDocVO.class);
        List<HmcActivityDocVO> records = hmcActivityDoctorVOPage.getRecords();
        List<Integer> doctorIdList = records.stream().map(HmcActivityDocVO::getDoctorId).collect(Collectors.toList());

        // 查询医生邀请用户数量
        if (CollUtil.isNotEmpty(doctorIdList)) {
            QueryActivityDoctorUserCountRequest queryDoctorRequest = new QueryActivityDoctorUserCountRequest();
            queryDoctorRequest.setActivitySource(HmcActivitySourceEnum.BA_ZI_BU_SHEN.getType());
            queryDoctorRequest.setDoctorIdList(doctorIdList);
            List<Map<String, Long>> doctorInviteUserCountMap = hmcUserApi.queryActivityDoctorInviteUserCount(queryDoctorRequest);
            records.forEach(record -> {
                Optional<Map<String, Long>> res = doctorInviteUserCountMap.stream().filter(doctorInviteUserCount -> doctorInviteUserCount.get("doctorId").equals(record.getDoctorId().longValue())).findFirst();
                res.ifPresent(map -> record.setCaseCount(map.get("userCount")));
            });
        }
        return Result.success(hmcActivityDoctorVOPage);
    }


}
