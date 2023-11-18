package com.yiling.ih.patient.api.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.location.dto.LocationDTO;
import com.yiling.ih.common.ApiPage;
import com.yiling.ih.common.ApiResult;
import com.yiling.ih.patient.api.HmcPatientApi;
import com.yiling.ih.patient.dto.*;
import com.yiling.ih.patient.dto.request.*;
import com.yiling.ih.patient.feign.HmcPatientFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * HMC患者API实现类
 *
 * @author: fan.shen
 * @date: 2022/6/7
 */
@Slf4j
@DubboService
public class HmcPatientApiImpl implements HmcPatientApi {

    @Autowired
    private HmcPatientFeignClient hmcPatientFeignClient;

    @DubboReference
    LocationApi locationApi;

    @Override
    public List<MyPatientDTO> myPatientList(Integer fromUserId) {
        ApiResult<List<MyPatientDTO>> apiResult = hmcPatientFeignClient.myPatientList(fromUserId);
        if (!apiResult.success()) {
            log.error("调用患者远程接口myPatientList出错");
        }
        return apiResult.getData();
    }

    @Override
    public void savePatient(SavePatientRequest request) {
        List<String> codeList = Lists.newArrayList();
        String regionCode = request.getIdCard().substring(0, 6);
        codeList.add(regionCode);
        List<LocationDTO> parentByCodeList = locationApi.getParentByCodeList(codeList);
        LocationDTO locationDTO = parentByCodeList.get(0);
        request.setProvinceCode(locationDTO.getParentCode());
        request.setCityCode(locationDTO.getCode());
        request.setRegionCode(regionCode);
        log.info("HmcPatientApi.savePatient 调用接口入参：{}", JSONUtil.toJsonStr(request));
        hmcPatientFeignClient.savePatient(request);
    }

    @Override
    public HmcSavePatientResultDTO saveMyPatient(SaveMyPatientRequest request) {
        log.info("saveMyPatient 入参：{}", JSONUtil.toJsonStr(request));
        ApiResult<HmcSavePatientResultDTO> apiResult = hmcPatientFeignClient.saveMyPatient(request);
        log.info("saveMyPatient 返参：{}", JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return null;
    }

    @Override
    public Page<HmcPatientDTO> queryPatientPage(HmcQueryPatientPageRequest request) {
        ApiResult<ApiPage<HmcPatientDTO>> apiPageApiResult = hmcPatientFeignClient.queryPatientPage(request);
        Page<HmcPatientDTO> result = request.getPage();
        if (!apiPageApiResult.success()) {
            log.error("调用患者远程接口queryPatientPage出错");
            return result;
        }
        ApiPage<HmcPatientDTO> data = apiPageApiResult.getData();
        result.setRecords(data.getList());
        result.setTotal(data.getTotal());
        return result;
    }

    @Override
    public Map<Long, Long> queryPatientByUserIdList(List<Long> userIdList) {
        ApiResult<Map<Long, Long>> mapApiResult = hmcPatientFeignClient.queryPatientByUserIdList(userIdList);
        if (!mapApiResult.success()) {
            log.error("调用患者远程接口queryPatientByUserIdList出错");
        }
        return mapApiResult.getData();
    }

    @Override
    public List<HmcPatientBindDoctorDTO> queryPatientBindDoctorByPatientId(QueryPatientBindDoctorRequest request) {
        ApiResult<List<HmcPatientBindDoctorDTO>> apiResult = hmcPatientFeignClient.queryPatientBindDoctorByPatientId(request);
        if (!apiResult.success()) {
            log.error("调用患者远程接口查询患者绑定医生列表出错");
        }
        return apiResult.getData();
    }

    @Override
    public void completeMyPatientDisease(CompletePatientDiseaseRequest request) {
        ApiResult<Boolean> apiResult = hmcPatientFeignClient.completeMyPatientDisease(request);
        if (!apiResult.success()) {
            log.error("调用患者远程接口完善既往史出错");
        }
    }

    @Override
    public MyPatientDetailDTO myPatientDetail(GetMyPatientDetailRequest request) {
        ApiResult<MyPatientDetailDTO> apiResult = hmcPatientFeignClient.myPatientDetail(request.getPatientId(), request.getFromUserId());
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return new MyPatientDetailDTO();
    }

    @Override
    public Boolean delMyPatient(DelMyPatientRequest request) {
        ApiResult<Boolean> apiResult = hmcPatientFeignClient.delMyPatient(request);
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean updateMyPatient(UpdateMyPatientRequest request) {
        log.info("[updateMyPatient]更新就诊人信息，入参:{}", JSONUtil.toJsonStr(request));
        ApiResult<Boolean> apiResult = hmcPatientFeignClient.updateMyPatient(request);
        log.info("[updateMyPatient]更新就诊人信息，返参:{}", JSONUtil.toJsonStr(apiResult));
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return Boolean.FALSE;
    }

    @Override
    public MyPatientDetailDTO getLastPatient(Long currentUserId) {
        ApiResult<MyPatientDetailDTO> apiResult = hmcPatientFeignClient.getLastPatient(currentUserId);
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return null;
    }
}