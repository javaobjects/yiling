package com.yiling.ih.user.api.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;
import com.tencent.healthcard.impl.HealthCardServerImpl;
import com.tencent.healthcard.model.CommonIn;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.basic.location.dto.LocationDTO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.ih.common.ApiResult;
import com.yiling.ih.patient.enums.IHErrorCode;
import com.yiling.ih.user.api.IHActivityPatientEducateApi;
import com.yiling.ih.user.dto.*;
import com.yiling.ih.user.feign.ActivityPatientEducateClient;
import com.yiling.ih.user.feign.IdCardFrontPhotoOcrClient;
import com.yiling.ih.user.feign.dto.response.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * C 端患教服务api
 *
 * @author: fan.shen
 * @date: 2022/9/7
 */
@Slf4j
@DubboService
public class IHActivityPatientEducateApiImpl implements IHActivityPatientEducateApi {

    @Autowired
    private IdCardFrontPhotoOcrClient idCardFrontPhotoOcrClient;

    @Autowired
    private ActivityPatientEducateClient activityPatientEducateClient;

    @DubboReference
    LocationApi locationApi;

    @Override
    public IdCardFrontPhotoOcrDTO idCardFrontPhotoOcr(String imageContent) {
        // 1、获取ocr输入参数
        ApiResult<HealthCardCommonInResponse> cardResult = idCardFrontPhotoOcrClient.getHealthCardCommonIn();
        HealthCardCommonInResponse data = cardResult.getData();
        if (!cardResult.success() || Objects.isNull(data)) {
            return null;
        }

        // 2、调用腾讯ocr接口
        HealthCardServerImpl healthCard = new HealthCardServerImpl(data.getAppSecret());
        CommonIn common = new CommonIn(data.getAppToken(), data.getRequestId(), data.getHospitalId(), data.getChannelNum());
        JSONObject idCardInfo = healthCard.ocrInfo(common, imageContent);
        log.info("腾讯ocr接口返回参数：{}", idCardInfo.toJSONString());

        JSONObject commonOut = idCardInfo.getJSONObject("commonOut");
        int resultCode = commonOut.getIntValue("resultCode");
        if (resultCode == 0) {
            JSONObject cardInfo = idCardInfo.getJSONObject("rsp").getJSONObject("cardInfo");
            String id = cardInfo.getString("id");
            String regionCode = id.substring(0, 6);
            List<LocationDTO> parentByCodeList = locationApi.getParentByCodeList(Collections.singletonList(regionCode));
            log.info("根据regionCode:{},获取结果:{}", regionCode, JSONUtil.toJsonStr(parentByCodeList));
            if (CollUtil.isNotEmpty(parentByCodeList) && parentByCodeList.size() > 0) {
                LocationDTO locationDTO = parentByCodeList.get(0);
                log.info("locationDTO:{}", locationDTO);
                cardInfo.put("provinceCode", locationDTO.getParentCode());
                cardInfo.put("cityCode", locationDTO.getCode());
                cardInfo.put("regionCode", regionCode);

                // 获取省市区名称
                String[] codesByNames = locationApi.getNamesByCodes(locationDTO.getParentCode(), locationDTO.getCode(), regionCode);
                log.info("[codesByNames]:{}", codesByNames);
                cardInfo.put("provinceName", codesByNames[0]);
                cardInfo.put("cityName", codesByNames[1]);
                cardInfo.put("regionName", codesByNames[2]);
            }

            if (cardInfo.containsKey("nation") && cardInfo.getString("nation").lastIndexOf("族") < 0) {
                cardInfo.put("nation", cardInfo.getString("nation") + "族");
            }
            cardInfo.put("idNo", cardInfo.getString("id"));
            return PojoUtils.map(cardInfo, IdCardFrontPhotoOcrDTO.class);
        } else {
            throw new BusinessException(IHErrorCode.ID_CARD_OCR);
        }
    }
//
//    public JSONObject ocrInfo(String imageContent) {
//        JSONObject obj = new JSONObject();
//        ApiResult<HealthCardCommonInResponse> cardResult = idCardFrontPhotoOcrClient.getHealthCardCommonIn();
//        HealthCardCommonInResponse data = cardResult.getData();
//        if (!cardResult.success() || Objects.isNull(data)) {
//            return null;
//        }
//        HealthCardServerImpl healthCard = new HealthCardServerImpl(data.getAppSecret());
//        CommonIn common = new CommonIn(data.getAppToken(), data.getRequestId(), data.getHospitalId(), data.getChannelNum());
//        JSONObject idCardInfo = healthCard.ocrInfo(common, imageContent);
//        log.info("腾讯ocr接口返回参数：{}" + idCardInfo.toJSONString());
//        JSONObject commonOut = idCardInfo.getJSONObject("commonOut");
//        int resultCode = commonOut.getIntValue("resultCode");
//        if (resultCode == 0) {
//            JSONObject cardInfo = idCardInfo.getJSONObject("rsp").getJSONObject("cardInfo");
//            String id = cardInfo.getString("id");
//            String regionCode = id.substring(0, 6);
//            List<LocationDTO> parentByCodeList = locationApi.getParentByCodeList(Collections.singletonList(regionCode));
//            LocationDTO locationDTO = parentByCodeList.get(0);
//            cardInfo.put("provinceCode", locationDTO.getParentCode());
//            cardInfo.put("cityCode", locationDTO.getCode());
//            cardInfo.put("regionCode", regionCode);
//            if (cardInfo.containsKey("nation") && cardInfo.getString("nation").lastIndexOf("族") < 0) {
//                cardInfo.put("nation", cardInfo.getString("nation") + "族");
//            }
//            cardInfo.put("idNo", cardInfo.getString("id"));
//            obj = cardInfo;
//        }
//        return obj;
//    }

    @Override
    public SavePatientRelDTO savePatient(AddPatientRelRequest request) {
        ApiResult<SavePatientRelResponse> apiResult = activityPatientEducateClient.savePatient(request);
        if (!apiResult.success() || Objects.isNull(apiResult.getData())) {
            return null;
        }
        return PojoUtils.map(apiResult.getData(), SavePatientRelDTO.class);
    }

    @Override
    public PatientDoctorRelDTO queryPatientDoctorRel(QueryPatientDoctorRelRequest request) {
        ApiResult<QueryPatientDoctorRelResponse> apiResult = activityPatientEducateClient.queryPatientDoctorRel(request.getActivityId(), request.getDoctorId(), request.getUserId());
        PatientDoctorRelDTO relDTO = new PatientDoctorRelDTO();
        if (!apiResult.success() || Objects.isNull(apiResult.getData())) {
            return relDTO;
        }
        PojoUtils.map(apiResult.getData(), relDTO);
        relDTO.setHasJoin(true);
        return relDTO;
    }

    @Override
    public Boolean checkIdCard(String idCard) {
        log.info("checkIdCard idCard:{}", JSONUtil.toJsonStr(idCard));
        ApiResult<Boolean> apiResult = activityPatientEducateClient.checkIdCard(idCard);
        log.info("checkIdCard result:{}", JSONUtil.toJsonStr(apiResult));
        if (apiResult.success() || Objects.nonNull(apiResult.getData())) {
            return apiResult.getData();
        }
        return apiResult.getData();
    }

    @Override
    public Map<Integer, Long> queryActivityDoctorCount(List<Integer> activityIdList) {
        ApiResult<Map<Integer, Long>> apiResult = activityPatientEducateClient.queryActivityDoctorCount(activityIdList);
        if (!apiResult.success() || Objects.isNull(apiResult.getData())) {
            return Maps.newHashMap();
        }
        return apiResult.getData();
    }

    @Override
    public DoctorAppInfoDTO getDoctorInfoByDoctorId(Integer doctorId) {
        ApiResult<GetDoctorInfoByDoctorIdResponse> apiResult = activityPatientEducateClient.getDoctorInfoByDoctorId(doctorId);
        if (!apiResult.success() || Objects.isNull(apiResult.getData())) {
            return null;
        }
        return convertDoctorInfo(apiResult.getData());
    }

    @Override
    public SaveActivityDocPatientRelDTO saveActivityDocPatient(AddActDocPatientRelRequest request) {
        ApiResult<SaveActivityDocPatientRelResponse> apiResult = activityPatientEducateClient.saveActivityDocPatient(request);
        log.info("[saveActivityDocPatient]保存医带患活动入参：{},返回：{}", JSONUtil.toJsonStr(request), JSONUtil.toJsonStr(apiResult));
        SaveActivityDocPatientRelDTO relDTO = new SaveActivityDocPatientRelDTO();
        if (!apiResult.success() || Objects.isNull(apiResult.getData())) {
            return relDTO;
        }
        return PojoUtils.map(apiResult.getData(), SaveActivityDocPatientRelDTO.class);
    }

    @Override
    public List<BaZiActivityDoctorCountDTO> queryBaZiActivityDoctorCount(List<Integer> activityIdList) {
        ApiResult<List<BaZiActivityDoctorCountDTO>> apiResult = activityPatientEducateClient.queryBaZiActivityDoctorCount(activityIdList);
        if (!apiResult.success() || Objects.isNull(apiResult.getData())) {
            return Lists.newArrayList();
        }
        return apiResult.getData();
    }

    private DoctorAppInfoDTO convertDoctorInfo(GetDoctorInfoByDoctorIdResponse response) {
        DoctorAppInfoDTO doctorAppInfoDTO = new DoctorAppInfoDTO();
        doctorAppInfoDTO.setDoctorName(response.getDoctorName());
        doctorAppInfoDTO.setProfession(response.getProfession());
        doctorAppInfoDTO.setHospitalName(response.getHospitalName());
        doctorAppInfoDTO.setHospitalDepartment(response.getHospitalDepartment());
        doctorAppInfoDTO.setMobile(response.getCellularphone());
        doctorAppInfoDTO.setNumber(response.getNumber());
        doctorAppInfoDTO.setIdentityFront(response.getIdentityfront());
        doctorAppInfoDTO.setIdentityReverse(response.getIdentityreverse());
        doctorAppInfoDTO.setDoctorNoteList(response.getDoctorsnoteList());
        doctorAppInfoDTO.setDoctorsNoteCertNo(response.getDoctorsnoteCertNo());
        doctorAppInfoDTO.setCertificateList(response.getCertificateList());
        doctorAppInfoDTO.setCertificateCertNo(response.getCertificateCertNo());
        return doctorAppInfoDTO;
    }
}
