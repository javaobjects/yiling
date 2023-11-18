package com.yiling.ih.user.feign;

import com.yiling.ih.common.ApiResult;
import com.yiling.ih.user.dto.AddActDocPatientRelRequest;
import com.yiling.ih.user.dto.AddPatientRelRequest;
import com.yiling.ih.user.dto.BaZiActivityDoctorCountDTO;
import com.yiling.ih.user.dto.QueryPatientDoctorRelRequest;
import com.yiling.ih.user.feign.dto.response.GetDoctorInfoByDoctorIdResponse;
import com.yiling.ih.user.feign.dto.response.QueryPatientDoctorRelResponse;
import com.yiling.ih.user.feign.dto.response.SaveActivityDocPatientRelResponse;
import com.yiling.ih.user.feign.dto.response.SavePatientRelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 互联网医院 患教活动接口调用
 *
 * @author: fan.shen
 * @date: 2022/9/7
 */
@FeignClient(name = "activityPatientEducateClient", url = "${ih.service.baseUrl}")
public interface ActivityPatientEducateClient {

    /**
     * 保存患者信息
     *
     * @param request 患者信息
     * @return
     */
    @PostMapping("/hmc/patientDoctorRel/saveRel")
    ApiResult<SavePatientRelResponse> savePatient(@RequestBody AddPatientRelRequest request);

    /**
     * 是否入组
     * @param activityId
     * @param doctorId
     * @param userId
     * @return
     */
    @GetMapping("/hmc/patientDoctorRel/queryPatientDoctorRel")
    ApiResult<QueryPatientDoctorRelResponse> queryPatientDoctorRel(@RequestParam("activityId") Long activityId, @RequestParam("doctorId") Integer doctorId, @RequestParam("userId") Integer userId);

    /**
     * 校验身份证是否存在
     * false-存在，TRUE-不存在
     * @param idCard
     * @return
     */
    @GetMapping("/hmc/patient/checkIdCard")
    ApiResult<Boolean> checkIdCard(@RequestParam("idCard") String idCard);

    /**
     * 查询活动下医生数量
     *
     * @param activityIdList
     * @return
     */
    @GetMapping("/hmc/doctor/queryActivityDoctorCount")
    ApiResult<Map<Integer,Long>> queryActivityDoctorCount(@RequestParam("activityIdList") List<Integer> activityIdList);

    /**
     * 根据医生ID获取医生详情
     * @param doctorId 医生ID
     * @return
     */
    @GetMapping("/hmc/doctor/getDoctorInfoById")
    ApiResult<GetDoctorInfoByDoctorIdResponse> getDoctorInfoByDoctorId(@RequestParam("doctorId") Integer doctorId);

    /**
     * 保存医带患
     * @param request
     * @return
     */
    @PostMapping("/hmc/patientDoctorRel/saveDoctorToPatientRel")
    ApiResult<SaveActivityDocPatientRelResponse> saveActivityDocPatient(AddActDocPatientRelRequest request);

    /**
     * 查询八子补肾活动医生数量
     * @param activityIdList
     * @return
     */
    @GetMapping("/hmc/doctorToBaZi/queryActivityDoctorCount")
    ApiResult<List<BaZiActivityDoctorCountDTO>> queryBaZiActivityDoctorCount(@RequestParam("activityIdList") List<Integer> activityIdList);
}
