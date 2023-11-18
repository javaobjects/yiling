package com.yiling.admin.hmc.patient.controller;

import com.yiling.admin.hmc.patient.form.QueryPatientBindDoctorForm;
import com.yiling.admin.hmc.patient.vo.PatientBindDoctorListVO;
import com.yiling.ih.patient.api.HmcPatientApi;
import com.yiling.ih.patient.dto.HmcPatientBindDoctorDTO;
import com.yiling.ih.patient.dto.HmcPatientDTO;
import com.yiling.ih.patient.dto.request.HmcQueryPatientPageRequest;
import com.yiling.ih.patient.dto.request.QueryPatientBindDoctorRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.hmc.patient.form.QueryPatientPageForm;
import com.yiling.admin.hmc.patient.vo.PatientVO;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.patient.api.PatientApi;
import com.yiling.hmc.patient.dto.PatientDTO;

import cn.hutool.core.util.IdcardUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @author: gxl
 * @date: 2022/4/7
 */
@RestController
@Api(tags = "患者信息管理")
@RequestMapping("/patient")
@Slf4j
public class PatientController extends BaseController {

    @DubboReference
    PatientApi patientApi;

    @DubboReference
    LocationApi locationApi;

    @DubboReference
    private HmcPatientApi hmcPatientApi;

    @ApiOperation(value = "分页查询", httpMethod = "GET")
    @GetMapping("queryPatientPage")
    public Result<Page<PatientVO>> queryPatientPage(QueryPatientPageForm queryPatientPageForm) {
        HmcQueryPatientPageRequest request = PojoUtils.map(queryPatientPageForm, HmcQueryPatientPageRequest.class);
        Page<HmcPatientDTO> hmcPatientDTOPage = hmcPatientApi.queryPatientPage(request);
        if (Objects.isNull(hmcPatientDTOPage) || hmcPatientDTOPage.getTotal() == 0) {
            return Result.success(queryPatientPageForm.getPage());
        }
        Page<PatientVO> patientVOPage = PojoUtils.map(hmcPatientDTOPage, PatientVO.class);
        patientVOPage.getRecords().forEach(patientVO -> {
            String[] locations = locationApi.getNamesByCodes(patientVO.getProvinceCode(), patientVO.getCityCode(), patientVO.getRegionCode());
            patientVO.setProvince(locations[0]).setCity(locations[1]).setRegion(locations[2]);
            patientVO.setIdCard(IdcardUtil.hide(patientVO.getIdCard(), 3, 14));
        });
        return Result.success(patientVOPage);
    }

    @ApiOperation(value = "患者绑定医生", httpMethod = "POST")
    @PostMapping("queryPatientBindDoctorByPatientId")
    public Result<List<PatientBindDoctorListVO>> queryPatientBindDoctorByPatientId(@RequestBody @Valid QueryPatientBindDoctorForm form) {
        QueryPatientBindDoctorRequest request = PojoUtils.map(form, QueryPatientBindDoctorRequest.class);
        List<HmcPatientBindDoctorDTO> hmcPatientDTOPage = hmcPatientApi.queryPatientBindDoctorByPatientId(request);
        return Result.success(PojoUtils.map(hmcPatientDTOPage, PatientBindDoctorListVO.class));
    }

}