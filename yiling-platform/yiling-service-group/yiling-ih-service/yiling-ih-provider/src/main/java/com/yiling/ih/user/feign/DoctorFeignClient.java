package com.yiling.ih.user.feign;

import java.util.List;

import com.yiling.ih.user.dto.*;
import com.yiling.ih.user.dto.request.*;
import com.yiling.ih.user.feign.dto.response.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.yiling.ih.common.ApiPage;
import com.yiling.ih.common.ApiResult;

/**
 * 互联网医院 医生模块接口调用
 *
 * @author: xuan.zhou
 * @date: 2022/6/8
 */
@FeignClient(name = "doctorFeignClient", url = "${ih.service.baseUrl}")
public interface DoctorFeignClient {

    /**
     * 根据医药代表id获取医生列表
     *
     * @param agentId    医药代表ID
     * @param doctorName 医生名称
     * @return
     */
    @GetMapping("/cms/doctor/getDoctorListByAgentId")
    ApiResult<List<GetDoctorListByAgentIdResponse>> getDoctorListByAgentId(@RequestParam("agentId") Long agentId, @RequestParam(value = "doctorName", required = false) String doctorName);

    /**
     * 销售助手APP-医生管理-根据医药代表id获取医生列表
     *
     * @param request 查询医药代表关联的医生列表入参
     * @return
     */
    @PostMapping("/cms/doctor/getDoctorListAppByAgentId")
    ApiResult<ApiPage<DoctorListAppInfoDTO>> getDoctorListAppByAgentId(@RequestBody QueryAppMrDoctorListRequest request);

    /**
     * 销售助手APP-医生管理-根据医生ID获取医生详情
     *
     * @param doctorId 医生ID
     * @return
     */
    @GetMapping("/cms/doctor/getDoctorInfoAppById")
    ApiResult<GetDoctorInfoByDoctorIdResponse> getDoctorInfoByDoctorId(@RequestParam("doctorId") Integer doctorId);

    /**
     * 医生分页列表
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctor/queryDoctorList")
    ApiResult<ApiPage<HmcDoctorListResponse>> queryDoctorList(@RequestBody QueryDoctorListRequest request);

    /**
     * 医带患活动医生分页列表
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctorToPatient/queryDoctorList")
    ApiResult<ApiPage<HmcDoctorListResponse>> activityQueryDoctorList(@RequestBody QueryDoctorListRequest request);

    /**
     * 八子补肾活动分页查询医生列表
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctorToBaZi/queryDoctorList")
    ApiResult<ApiPage<HmcDoctorListResponse>> activityBaZiQueryDoctorList(@RequestBody QueryDoctorListRequest request);

    /**
     * 活动医生分页
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctor/queryActivityDoctorList")
    ApiResult<ApiPage<HmcActivityDoctorDTO>> queryActivityDoctorList(@RequestBody QueryActivityDoctorListRequest request);

    /**
     * 验收医生是否参加活动
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctor/verifyActivityDoctor")
    ApiResult<Boolean> verifyActivityDoctor(@RequestBody VerifyActivityDoctorRequest request);

    /**
     * 保存活动医生
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctor/saveActivityDoctor")
    ApiResult saveActivityDoctor(@RequestBody SaveActivityDoctorRequest request);

    /**
     * 保存医带患活动医生
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctorToPatient/saveActivityDoctor")
    ApiResult saveActivityDocPatientDoctor(@RequestBody SaveActivityDoctorRequest request);

    /**
     * 保存八子补肾活动医生
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctorToBaZi/saveActivityDoctor")
    ApiResult saveActivityBaZiDoctor(@RequestBody SaveActivityDoctorRequest request);

    /**
     * 移除活动医生
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctor/deleteActivityDoctor")
    ApiResult deleteActivityDoctor(@RequestBody DeleteActivityDoctorRequest request);

    /**
     * 更新活动医生状态
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctorToPatient/updateActivityDoctorStatus")
    ApiResult updateActivityDoctorStatus(@RequestBody DeleteActivityDoctorRequest request);

    /**
     * 移除八子补肾活动医生状态
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctorToBaZi/deleteActivityDoctor")
    ApiResult deleteBaZiActivityDoctor(@RequestBody DeleteActivityDoctorRequest request);

    /**
     * 查询第一执业机构
     *
     * @param hospitalName
     * @return
     */
    @GetMapping("/hmc/doctor/queryVocationalHospital")
    ApiResult<List<HmcVocationalHospitalDTO>> queryVocationalHospitalList(@RequestParam("hospitalName") String hospitalName);

    /**
     * 根据活动id和医生id查询 尚未建立关系的医生id
     *
     * @param activityId
     * @param doctorIdList
     * @return
     */
    @GetMapping("/hmc/doctor/checkActivityDoctor")
    ApiResult<List<Integer>> checkActivityDoctor(@RequestParam(value = "activityId") Integer activityId, @RequestParam(value = "doctorIdList") List<Integer> doctorIdList);

    /**
     * 获取医生详情集合
     *
     * @param doctorIds
     * @return
     */
    @GetMapping("/hmc/doctor/getDoctorInfoByIds")
    ApiResult<List<HmcDoctorInfoDTO>> getDoctorInfoByIds(@RequestParam(value = "doctorIds") List<Integer> doctorIds);

    /**
     * 医生是否开启问诊服务项
     *
     * @param doctorId
     * @return
     */
    @GetMapping("/hmc/doctor/checkDoctorDiagnosis")
    ApiResult<Boolean> checkDoctorDiagnosis(@RequestParam(value = "doctorId") Integer doctorId);

    /**
     * 运营后台-视频文章管理-添加所属医生
     *
     * @param request 查询医生入参
     * @return
     */
    @PostMapping("/hmc/doctor/getDoctorList")
    ApiResult<ApiPage<HmcDoctorInfoDTO>> queryDoctorPage(@RequestBody QueryDoctorPageRequest request);

    /**
     * 健康测评-根据科室查询推荐医生
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctor/queryRecommendDoctor")
    ApiResult<List<RecommendDoctorDTO>> queryRecommendDoctor(@RequestBody QueryRecommendDoctorRequest request);

    /**
     * 查看用户是否和医生绑定-医带患
     *
     * false是已绑定
     * true是没绑定
     * @param request
     * @return
     */
    @PostMapping("/hmc/patientDoctorRel/checkUserDoctorRel")
    ApiResult<Boolean> checkUserDoctorRel(CheckUserDoctorRelRequest request);

    /**
     * 医带患活动医生分页
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctorToPatient/queryActivityDoctorList")
    ApiResult<ApiPage<HmcActivityDocDTO>> queryActivityDocPage(@RequestBody QueryActivityDoctorListRequest request);

    /**
     * 八子补肾活动医生分页
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctorToBaZi/queryActivityDoctorList")
    ApiResult<ApiPage<HmcActivityDocDTO>> queryActivityBaZiDoctorPage(@RequestBody QueryActivityDoctorListRequest request);

    /**
     * 查看活动参与医生数量患者数量-医带患
     * @param request
     * @return
     */
    @PostMapping("/hmc/patientDoctorRel/getActivityDoctorPatientCount")
    ApiResult<List<ActivityDocPatientCountResponse>> getActivityDoctorPatientCount(GetActivityDoctorPatientCountRequest request);

    /**
     * 医带患活动患者分页
     *
     * @param request
     * @return
     */
    @PostMapping("/hmc/patientDoctorRel/getActivityDoctorToPatientList")
    ApiResult<ApiPage<ActivityDocPatientResponse>> queryActivityDocPatientPage(@RequestBody QueryActivityDocPatientListRequest request);

    /**
     * 获取患者详情
     * @param id
     * @return
     */
    @GetMapping("/hmc/patientDoctorRel/getActivityDoctorToPatientInfo")
    ApiResult<HmcActivityDocPatientDetailDTO> queryActivityDocPatientDetail(@RequestParam(value = "id")Integer id);

    /**
     * 医带患患者审核
     * @param request
     * @return
     */
    @PostMapping("/hmc/patientDoctorRel/auditActivityDoctorToPatient")
    ApiResult<Boolean> activityDocPatientAudit(ActivityDocPatientAuditRequest request);

    /**
     * 根据医生id和医生姓名查询医生id接口
     * @param request
     * @return
     */
    @PostMapping("/hmc/doctor/getDoctorIdByIdName")
    ApiResult<List<Long>> queryDoctorByIdAndName(QueryDoctorRequest request);

    /**
     * 导入活动医生
     * @param requestList
     * @return
     */
    @PostMapping("/hmc/doctorToPatient/saveActivityDoctorBatch")
    ApiResult<List<HmcActivityImportDocResDTO>> importActivityDoc(List<HMCImportActivityDocRequest> requestList);
}
