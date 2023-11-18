package com.yiling.open.cms.activity.controller;

import cn.hutool.core.util.StrUtil;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.hmc.activity.api.HMCActivityApi;
import com.yiling.hmc.activity.dto.ActivityDocToPatientDTO;
import com.yiling.open.cms.activity.form.GetDoctorQrCodeForm;
import com.yiling.open.cms.activity.form.QueryActivityDocPatientForm;
import com.yiling.open.cms.activity.form.QueryActivityPatientEducateForm;
import com.yiling.open.cms.activity.vo.ActivityDocPatientDetailVO;
import com.yiling.open.cms.activity.vo.ActivityDocPatientVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 互联网医院-医带患活动控制器
 *
 * @author: fan.shen
 * @date: 2023-02-01
 */
@Api(tags = "医带患活动控制器")
@RestController
@RequestMapping("/iHActivityDocPatient")
public class IHActivityDocPatientController extends BaseController {

    @DubboReference
    HMCActivityApi hmcActivityApi;

    @Autowired
    private FileService fileService;

    @ApiOperation(value = "医带患活动")
    @PostMapping("/getActivityByIdList")
    @Log(title = "医带患活动", businessType = BusinessTypeEnum.OTHER)
    public Result<List<ActivityDocPatientVO>> getActivityByIdList(@RequestBody QueryActivityPatientEducateForm form) {
        List<ActivityDocToPatientDTO> list = hmcActivityApi.queryActivityByIdList(form.getIdList());
        List<ActivityDocPatientVO> result = PojoUtils.map(list, ActivityDocPatientVO.class);
        return Result.success(result);
    }

    @ApiOperation(value = "获取医带患活动详情")
    @PostMapping("/getActivityDocPatientById")
    @Log(title = "医带患活动", businessType = BusinessTypeEnum.OTHER)
    public Result<ActivityDocPatientDetailVO> getActivityDocPatientById(@RequestBody QueryActivityDocPatientForm form) {
        ActivityDocToPatientDTO patientDTO = hmcActivityApi.queryActivityById(form.getId());
        ActivityDocPatientDetailVO detailVO = PojoUtils.map(patientDTO, ActivityDocPatientDetailVO.class);
        if(StrUtil.isNotBlank(patientDTO.getActivityHeadPic())) {
            String url = fileService.getUrl(patientDTO.getActivityHeadPic(), FileTypeEnum.HMC_ACTIVITY_PIC);
            detailVO.setActivityHeadPic(url);
        }
        return Result.success(detailVO);
    }

    @ApiOperation(value = "获取医生二维码")
    @PostMapping("/getDoctorQrCodeById")
    @Log(title = "获取医生二维码", businessType = BusinessTypeEnum.OTHER)
    public Result<String> getDoctorQrCodeById(@RequestBody @Validated GetDoctorQrCodeForm form) {
        String qrCodeUrl = hmcActivityApi.getQrCodeUrl(null, form.getDoctorId().intValue());
        return Result.success(qrCodeUrl);
    }

}