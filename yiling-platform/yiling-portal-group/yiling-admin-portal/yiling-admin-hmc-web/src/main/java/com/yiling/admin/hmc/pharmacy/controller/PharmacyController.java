package com.yiling.admin.hmc.pharmacy.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaCodeLineColor;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.hmc.pharmacy.form.PharmacyBaseForm;
import com.yiling.admin.hmc.pharmacy.form.PharmacyPageForm;
import com.yiling.admin.hmc.pharmacy.form.SubmitPharmacyForm;
import com.yiling.admin.hmc.pharmacy.form.UpdatePharmacyStatusForm;
import com.yiling.admin.hmc.pharmacy.vo.PharmacyVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.MiniAppEnvEnum;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.bo.FileInfo;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.pharmacy.api.HmcPharmacyApi;
import com.yiling.hmc.pharmacy.dto.PharmacyDTO;
import com.yiling.hmc.pharmacy.dto.request.PharmacyPageRequest;
import com.yiling.hmc.pharmacy.dto.request.SubmitPharmacyRequest;
import com.yiling.hmc.pharmacy.dto.request.UpdatePharmacyStatusRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.util.List;

/**
 * 终端药店
 *
 * @author: fan.shen
 * @date: 2023/5/6
 */
@Api(tags = "终端药店")
@RestController
@RequestMapping("/pharmacy")
@Slf4j
public class PharmacyController extends BaseController {

    @Value("${env.name}")
    private String envName;

    @DubboReference
    HmcPharmacyApi pharmacyApi;

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private FileService fileService;

    @ApiOperation("01、保存终端药店")
    @PostMapping("savePharmacy")
    @Log(title = "保存药店", businessType = BusinessTypeEnum.INSERT)
    public Result<Long> submitMeetingSign(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid SubmitPharmacyForm form) {

        SubmitPharmacyRequest request = PojoUtils.map(form, SubmitPharmacyRequest.class);

        boolean checkResult = pharmacyApi.check(request);
        if(checkResult) {
            return Result.failed("当前企业已经添加过！");
        }

        request.setOpUserId(currentAdminInfo.getCurrentUserId());
        return Result.success(pharmacyApi.savePharmacy(request));
    }

    @ApiOperation("02、停启用")
    @PostMapping("updatePharmacyStatus")
    @Log(title = "停启用", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> updatePharmacyStatus(@CurrentUser CurrentAdminInfo currentAdminInfo, @RequestBody @Valid UpdatePharmacyStatusForm form) {
        UpdatePharmacyStatusRequest request = PojoUtils.map(form, UpdatePharmacyStatusRequest.class);
        return Result.success(pharmacyApi.updatePharmacyStatus(request));
    }

    @ApiOperation("03、终端药店列表")
    @PostMapping("pharmacyPageList")
    @Log(title = "终端药店列表", businessType = BusinessTypeEnum.OTHER)
    public Result<Page<PharmacyVO>> pharmacyPageList(@RequestBody @Valid PharmacyPageForm form) {
        PharmacyPageRequest request = PojoUtils.map(form, PharmacyPageRequest.class);
        Page<PharmacyDTO> pageList = pharmacyApi.pharmacyPageList(request);
        Page<PharmacyVO> result = PojoUtils.map(pageList, PharmacyVO.class);
        return Result.success(result);
    }

    @ApiOperation("05、终端药店下拉")
    @GetMapping("pharmacyList")
    public Result<List<PharmacyVO>> pharmacyList() {
        List<PharmacyDTO> list = pharmacyApi.pharmacyList();
        return Result.success(PojoUtils.map(list, PharmacyVO.class));
    }

    @ApiOperation("04、商家问诊码")
    @PostMapping("pharmacyQrCode")
    @Log(title = "商家问诊码", businessType = BusinessTypeEnum.OTHER)
    public Result<String> pharmacyQrCode(@RequestBody @Valid PharmacyBaseForm form) {
        PharmacyDTO pharmacyDTO = pharmacyApi.getById(form.getId());
        if (StrUtil.isNotBlank(pharmacyDTO.getQrCode())) {
            return Result.success(fileService.getUrl(pharmacyDTO.getQrCode(), FileTypeEnum.ACTIVITY_DOCTOR_QRCODE));
        }
        String miniAppEnv = MiniAppEnvEnum.getBySelfEnv(envName).getMiniAppEnv();
        String scene = String.format("qr=eid:%s", pharmacyDTO.getEid());
        WxMaCodeLineColor color = new WxMaCodeLineColor("20", "145", "255");
        try {
            // todo 页面路径
            String page = "";
            File wxCode = this.wxMaService.getQrcodeService().createWxaCodeUnlimit(scene, page, false, miniAppEnv, 430, false, color, false);
            FileInfo upload = fileService.upload(wxCode, FileTypeEnum.ACTIVITY_DOCTOR_QRCODE);
            pharmacyDTO.setQrCode(upload.getKey());
            pharmacyApi.updatePharmacyQrCode(pharmacyDTO);
            String url = fileService.getUrl(upload.getKey(), FileTypeEnum.ACTIVITY_DOCTOR_QRCODE);
            return Result.success(url);
        } catch (Exception e) {
            log.error("生成商家问诊码报错{}", e.getMessage());
            return Result.failed("生成商家问诊码失败");
        }

    }

}