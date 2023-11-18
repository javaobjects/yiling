package com.yiling.ih.patient.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.ih.patient.dto.*;
import com.yiling.ih.patient.dto.request.*;

import java.util.List;
import java.util.Map;

/**
 * HMC患者API
 *
 * @author: fan.shen
 * @date: 2022/8/23
 */
public interface HmcPatientApi {

    /**
     * 我的就诊人列表
     * @return
     */
    List<MyPatientDTO> myPatientList(Integer fromUserId);

    /**
     * 保存患者信息
     *
     * @param request
     * @return
     */
    void savePatient(SavePatientRequest request);

    /**
     * 保存我的就诊人信息
     *
     * @param request
     * @return
     */
    HmcSavePatientResultDTO saveMyPatient(SaveMyPatientRequest request);

    /**
     * 分页查询患者信息
     *
     * @param request
     * @return
     */
    Page<HmcPatientDTO> queryPatientPage(HmcQueryPatientPageRequest request);

    /**
     * 根据UserId查询患者信息
     *
     * @param userIdList
     * @return
     */
    Map<Long, Long> queryPatientByUserIdList(List<Long> userIdList);

    /**
     * 查询患者绑定医生
     * @param request
     * @return
     */
    List<HmcPatientBindDoctorDTO> queryPatientBindDoctorByPatientId(QueryPatientBindDoctorRequest request);

    /**
     * 完善既往史
     * @param request
     */
    void completeMyPatientDisease(CompletePatientDiseaseRequest request);

    /**
     * 我的就诊人详情
     * @param request
     * @return
     */
    MyPatientDetailDTO myPatientDetail(GetMyPatientDetailRequest request);

    /**
     * 删除我的就诊人
     * @param request
     * @return
     */
    Boolean delMyPatient(DelMyPatientRequest request);

    /**
     * 编辑我的就诊人
     * @param request
     * @return
     */
    Boolean updateMyPatient(UpdateMyPatientRequest request);

    /**
     * 获取上一个就诊人
     * @param currentUserId
     * @return
     */
    MyPatientDetailDTO getLastPatient(Long currentUserId);
}
