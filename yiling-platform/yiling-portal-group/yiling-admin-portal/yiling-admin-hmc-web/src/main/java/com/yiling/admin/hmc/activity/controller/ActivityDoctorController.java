package com.yiling.admin.hmc.activity.controller;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.hmc.activity.form.DeleteActivityDoctorForm;
import com.yiling.admin.hmc.activity.form.QueryActivityDoctorPageForm;
import com.yiling.admin.hmc.activity.form.SaveActivityDoctorForm;
import com.yiling.admin.hmc.activity.vo.HmcActivityDoctorVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.MiniAppEnvEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.HmcActivityDoctorDTO;
import com.yiling.ih.user.dto.request.DeleteActivityDoctorRequest;
import com.yiling.ih.user.dto.request.HmcActivityDoctorQrcodeUrlQuest;
import com.yiling.ih.user.dto.request.QueryActivityDoctorListRequest;
import com.yiling.ih.user.dto.request.SaveActivityDoctorRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.core.collection.CollUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2022/9/7
 */
@RestController
@RequestMapping("/activity/doctor")
@Api(tags = "活动医生")
@Slf4j
public class ActivityDoctorController extends BaseController {

    @DubboReference
    DoctorApi doctorApi;

    @Autowired
    private WxMaService wxService;
    @Autowired
    private FileService fileService;

    @Value("${env.name}")
    private String envName;

    @ApiOperation(value = "医生列表")
    @PostMapping("queryActivityDoctorPage")
    public Result<Page<HmcActivityDoctorVO>> queryActivityDoctorPage(@RequestBody @Valid QueryActivityDoctorPageForm form) {
        QueryActivityDoctorListRequest request = new QueryActivityDoctorListRequest();
        PojoUtils.map(form, request);
        Page<HmcActivityDoctorDTO> hmcActivityDoctorDTOPage = doctorApi.queryActivityDoctorList(request);
        if (CollUtil.isEmpty(hmcActivityDoctorDTOPage.getRecords())) {
            return Result.success(form.getPage());
        }
        Page<HmcActivityDoctorVO> hmcActivityDoctorVOPage = PojoUtils.map(hmcActivityDoctorDTOPage, HmcActivityDoctorVO.class);
        hmcActivityDoctorVOPage.getRecords().forEach(hmcActivityDoctorVO -> {
            hmcActivityDoctorVO.setQrcodeUrl(fileService.getUrl(hmcActivityDoctorVO.getQrcodeUrl(), FileTypeEnum.ACTIVITY_DOCTOR_QRCODE));

        });
        return Result.success(hmcActivityDoctorVOPage);
    }

    @ApiOperation(value = "保存活动医生")
    @PostMapping("save")
    public Result<Boolean> save(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid SaveActivityDoctorForm form) {
        SaveActivityDoctorRequest request = new SaveActivityDoctorRequest();
        request.setCreateUser(adminInfo.getCurrentUserId().intValue());

        PojoUtils.map(form, request);
        List<Integer> checkActivityDoctor = doctorApi.checkActivityDoctor(form.getActivityId(), request.getHmcActivityDoctorQrcodeUrlFormList().stream().map(HmcActivityDoctorQrcodeUrlQuest::getDoctorId).collect(Collectors.toList()));
        if (CollUtil.isNotEmpty(checkActivityDoctor)) {
            for (int i = 0; i < request.getHmcActivityDoctorQrcodeUrlFormList().size(); i++) {
                if (!checkActivityDoctor.contains(request.getHmcActivityDoctorQrcodeUrlFormList().get(i).getDoctorId())) {
                    request.getHmcActivityDoctorQrcodeUrlFormList().remove(i);
                }
            }
        }
        try {
            this.generateQrcode(request.getHmcActivityDoctorQrcodeUrlFormList(), form.getActivityId());
        } catch (Exception e) {
            log.error("生成活动码报错俄{}", e.getMessage());
            return Result.failed("生成活动码失败");
        }
        doctorApi.saveActivityDoctor(request);
        return Result.success(true);
    }

    @ApiOperation(value = "移除活动医生")
    @PostMapping("deleteActivityDoctor")
    public Result<Boolean> deleteActivityDoctor(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid DeleteActivityDoctorForm form) {
        DeleteActivityDoctorRequest request = new DeleteActivityDoctorRequest();
        PojoUtils.map(form, request);
        doctorApi.deleteActivityDoctor(request);
        return Result.success(true);
    }


    /**
     * 生成活动码
     *
     * @param hmcActivityDoctorQrcodeUrlFormList
     * @param activityId
     * @throws Exception
     */
    private void generateQrcode(List<HmcActivityDoctorQrcodeUrlQuest> hmcActivityDoctorQrcodeUrlFormList, Integer activityId) throws Exception {
        String miniAppEnv = MiniAppEnvEnum.getBySelfEnv(envName).getMiniAppEnv();
        for (HmcActivityDoctorQrcodeUrlQuest form : hmcActivityDoctorQrcodeUrlFormList) {
            String scene = String.format("qr=actId:%s_docId:%s", activityId, form.getDoctorId());
            WxMaCodeLineColor color = new WxMaCodeLineColor("20", "145", "255");
            final File wxCode = this.wxService.getQrcodeService().createWxaCodeUnlimit(scene, "pagesSub/main/activity/index", false, miniAppEnv, 430, false, color, false);
            FileInfo upload = fileService.upload(wxCode, FileTypeEnum.ACTIVITY_DOCTOR_QRCODE);
            form.setQrcodeUrl(upload.getKey());
        }

    }
}