package com.yiling.ih.user.api;

import com.yiling.ih.user.dto.*;

import java.util.List;
import java.util.Map;

/**
 * 患教活动api
 * @author: fan.shen
 * @date: 2022/9/5
 */
public interface IHActivityPatientEducateApi {


    /**
     * 身份证照片正面照 OCR
     * @param imageContent
     * @return
     */
    IdCardFrontPhotoOcrDTO idCardFrontPhotoOcr(String imageContent);

    /**
     * 保存患者信息
     * @param request
     * @return
     */
    SavePatientRelDTO savePatient(AddPatientRelRequest request);

    /**
     * 是否入组
     * @param request
     * @return
     */
    PatientDoctorRelDTO queryPatientDoctorRel(QueryPatientDoctorRelRequest request);

    /**
     * 校验身份证是否存在
     * @param idCard
     * @return
     */
    Boolean checkIdCard(String idCard);

    /**
     * 查询活动下医生数量
     * @param activityIdList
     * @return
     */
    Map<Integer,Long> queryActivityDoctorCount(List<Integer> activityIdList);

    /**
     * 获取医生信息
     * @param doctorId
     * @return
     */
    DoctorAppInfoDTO getDoctorInfoByDoctorId(Integer doctorId);

    /**
     * 保存医带患活动信息
     * @param request
     * @return
     */
    SaveActivityDocPatientRelDTO saveActivityDocPatient(AddActDocPatientRelRequest request);

    /**
     * 查询八子补肾活动下医生数量
     * @param activityIdList
     * @return
     */
    List<BaZiActivityDoctorCountDTO> queryBaZiActivityDoctorCount(List<Integer> activityIdList);
}
