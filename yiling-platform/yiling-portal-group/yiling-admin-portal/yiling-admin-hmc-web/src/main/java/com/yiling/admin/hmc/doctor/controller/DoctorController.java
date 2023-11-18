package com.yiling.admin.hmc.doctor.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.hmc.doctor.form.QueryDoctorPageForm;
import com.yiling.admin.hmc.doctor.vo.HmcDoctorListVO;
import com.yiling.admin.hmc.doctor.vo.HmcVocationalHospitalVO;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.dto.HmcDoctorListDTO;
import com.yiling.ih.user.dto.HmcVocationalHospitalDTO;
import com.yiling.ih.user.dto.request.QueryDoctorListRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: gxl
 * @date: 2022/9/7
 */
@RestController
@Api(tags = "以岭互联网医院医生")
@RequestMapping("/doctor")
@Slf4j
public class DoctorController extends BaseController {

    @DubboReference
    DoctorApi doctorApi;

    @ApiOperation(value = "分页查询(活动选择医生)", httpMethod = "GET")
    @GetMapping("page")
    public Result<Page<HmcDoctorListVO>> queryPage(@Valid QueryDoctorPageForm form){
        QueryDoctorListRequest request = new QueryDoctorListRequest();
        PojoUtils.map(form,request);
        Page<HmcDoctorListDTO> hmcDoctorListDTOPage = doctorApi.queryDoctorList(request);
        Page<HmcDoctorListVO> page = PojoUtils.map(hmcDoctorListDTOPage,HmcDoctorListVO.class);
        return Result.success(page);
    }

    @ApiOperation(value = "分页查询(医带患活动选择医生)", httpMethod = "GET")
    @GetMapping("activity/docPatient")
    public Result<Page<HmcDoctorListVO>> activityQueryDocPage(@Valid QueryDoctorPageForm form){
        QueryDoctorListRequest request = new QueryDoctorListRequest();
        PojoUtils.map(form,request);
        Page<HmcDoctorListDTO> hmcDoctorListDTOPage = doctorApi.activityQueryDoctorList(request);
        Page<HmcDoctorListVO> page = PojoUtils.map(hmcDoctorListDTOPage,HmcDoctorListVO.class);
        return Result.success(page);
    }

    @ApiOperation(value = "分页查询(八子补肾活动选择医生)", httpMethod = "GET")
    @GetMapping("activityBaZi/docPatient")
    public Result<Page<HmcDoctorListVO>> activityBaZiQueryDocPage(@Valid QueryDoctorPageForm form){
        QueryDoctorListRequest request = new QueryDoctorListRequest();
        PojoUtils.map(form,request);
        Page<HmcDoctorListDTO> hmcDoctorListDTOPage = doctorApi.activityBaZiQueryDocPage(request);
        Page<HmcDoctorListVO> page = PojoUtils.map(hmcDoctorListDTOPage,HmcDoctorListVO.class);
        return Result.success(page);
    }

    @ApiOperation(value = "查询第一执业机构", httpMethod = "GET")
    @GetMapping("queryVocationalHospitalList")
    public Result<List<HmcVocationalHospitalVO>>  queryVocationalHospitalList(@RequestParam(value = "hospitalName",required = false) String hospitalName){
        List<HmcVocationalHospitalDTO> hmcVocationalHospitalDTOS = doctorApi.queryVocationalHospitalList(hospitalName);
        List<HmcVocationalHospitalVO> hmcVocationalHospitalVOS = PojoUtils.map(hmcVocationalHospitalDTOS,HmcVocationalHospitalVO.class);
        return Result.success(hmcVocationalHospitalVOS);
    }
}