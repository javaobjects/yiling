package com.yiling.ih.patient.feign;

import java.util.List;
import java.util.Map;

import com.yiling.ih.patient.dto.*;
import com.yiling.ih.patient.dto.request.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.yiling.ih.common.ApiPage;
import com.yiling.ih.common.ApiResult;
import com.yiling.ih.config.FeignConfig;

/**
 * 患者远程服务
 *
 * @author: fan.shen
 * @date: 2022/8/23
 */
@FeignClient(name = "hmcPatientFeignClient", url = "${ih.service.baseUrl}", configuration = FeignConfig.class)
public interface HmcPatientFeignClient {

    /**
     * 保存患者信息
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/patient/savePatient")
    void savePatient(SavePatientRequest request);

    /**
     * 保存患者信息
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/patient/savePatient")
    ApiResult<HmcSavePatientResultDTO> saveMyPatient(SaveMyPatientRequest request);

    /**
     * 分页查询患者信息
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/patient/queryPatientPage")
    ApiResult<ApiPage<HmcPatientDTO>> queryPatientPage(HmcQueryPatientPageRequest request);

    /**
     * 根据userIdList查询患者信息
     *
     * @param userIdList
     * @return Map<key-userId, value-绑定的患者数量>
     */
    @GetMapping("/hmc/patient/queryPatientByUserIdList")
    ApiResult<Map<Long, Long>> queryPatientByUserIdList(@RequestParam("userIdList") List<Long> userIdList);

    /**
     * 查询患者绑定医生列表
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/patient/getDoctorListByPatientId")
    ApiResult<List<HmcPatientBindDoctorDTO>> queryPatientBindDoctorByPatientId(@RequestBody QueryPatientBindDoctorRequest request);

    /**
     * 我的就诊人列表
     *
     * @return
     */
    @GetMapping("/hmc/patient/patientList")
    ApiResult<List<MyPatientDTO>> myPatientList(@RequestParam("fromUserId") Integer fromUserId);

    /**
     * 完善既往史
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/patient/completeDisease")
    ApiResult<Boolean> completeMyPatientDisease(CompletePatientDiseaseRequest request);

    /**
     * 我的就诊人详情
     *
     * @param patientId
     * @return
     */
    @GetMapping("/hmc/patient/getPatientInfo")
    ApiResult<MyPatientDetailDTO> myPatientDetail(@RequestParam("patientId") Integer patientId, @RequestParam("fromUserId") Integer fromUserId);

    /**
     * 删除就诊人
     *
     * @return
     */
    @PostMapping("/hmc/patient/deletePatient")
    ApiResult<Boolean> delMyPatient(@RequestBody DelMyPatientRequest request);

    /**
     * 编辑就诊人
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/patient/updatePatient")
    ApiResult<Boolean> updateMyPatient(@RequestBody UpdateMyPatientRequest request);

    /**
     * 获取上一个就诊人
     *
     * @param fromUserId
     * @return
     */
    @GetMapping("/hmc/patient/getLastDiagnosisRecordPatient")
    ApiResult<MyPatientDetailDTO> getLastPatient(@RequestParam("fromUserId") Long fromUserId);
}
