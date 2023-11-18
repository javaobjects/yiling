package com.yiling.ih.user.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.ih.user.dto.*;
import com.yiling.ih.user.dto.request.*;

/**
 * @author: xuan.zhou
 * @date: 2022/6/8
 */
public interface DoctorApi {

    /**
     * 根据医药代表ID获取关联的医生列表
     *
     * @param request
     * @return java.util.List<com.yiling.ih.user.feign.dto.GetDoctorListByAgentIdResponse>
     * @author xuan.zhou
     * @date 2022/6/8
     **/
    List<DoctorInfoDTO> listByMrId(QueryMrDoctorListRequest request);

    /**
     * 销售助手APP-医生管理-根据医药代表ID获取关联的医生列表
     *
     * @param request
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.yiling.ih.user.dto.DoctorAppInfoDTO>
     * @author benben.jia
     * @date 2022/6/14
     */
    Page<DoctorListAppInfoDTO> listAppByMrId(QueryAppMrDoctorListRequest request);

    /**
     * 销售助手APP-医生管理-根据医生ID获取医生详情
     *
     * @param doctorId 医生id
     * @return
     */
    DoctorAppInfoDTO getDoctorInfoByDoctorId(Integer doctorId);

    /**
     * 活动选择医生列表
     *
     * @return
     */
    Page<HmcDoctorListDTO> queryDoctorList(QueryDoctorListRequest request);

    /**
     * 医带患活动查询医生列表
     * @param request
     * @return
     */
    Page<HmcDoctorListDTO> activityQueryDoctorList(QueryDoctorListRequest request);

    /**
     * 八子补肾活动查询医生列表
     * @param request
     * @return
     */
    Page<HmcDoctorListDTO> activityBaZiQueryDocPage(QueryDoctorListRequest request);

    /**
     * 活动医生分页
     *
     * @param request
     * @return
     */
    Page<HmcActivityDoctorDTO> queryActivityDoctorList(QueryActivityDoctorListRequest request);

    /**
     * 医带患活动医生分页
     *
     * @param request
     * @return
     */
    Page<HmcActivityDocDTO> queryActivityDocPage(QueryActivityDoctorListRequest request);

    /**
     * 八子补肾活动医生分页
     *
     * @param request
     * @return
     */
    Page<HmcActivityDocDTO> queryActivityBaZiPage(QueryActivityDoctorListRequest request);

    /**
     * 活动医生分页
     *
     * @param request
     * @return
     */
    Boolean verifyActivityDoctor(VerifyActivityDoctorRequest request);

    /**
     * 保存活动医生
     *
     * @param request
     */
    void saveActivityDoctor(SaveActivityDoctorRequest request);

    /**
     * 保存医带患活动医生
     *
     * @param request
     */
    void saveActivityDocPatientDoctor(SaveActivityDoctorRequest request);

    /**
     * 保存医带患活动医生
     *
     * @param request
     */
    void saveActivityBaZiDoctor(SaveActivityDoctorRequest request);

    /**
     * 移除活动医生
     *
     * @param request
     */
    void deleteActivityDoctor(DeleteActivityDoctorRequest request);

    /**
     * 更新活动医生状态
     *
     * @param request
     */
    void updateActivityDoctorStatus(DeleteActivityDoctorRequest request);

    /**
     * 移除八子补肾活动医生
     *
     * @param request
     */
    void deleteActivityBaZiDoctorStatus(DeleteActivityDoctorRequest request);

    /**
     * 查询第一执业机构
     *
     * @param hospitalName
     * @return
     */
    List<HmcVocationalHospitalDTO> queryVocationalHospitalList(String hospitalName);

    /**
     * 医生id去重
     *
     * @param activityId
     * @param doctorIdList
     * @return
     */
    List<Integer> checkActivityDoctor(Integer activityId, List<Integer> doctorIdList);

    /**
     * 批量获取医生信息
     *
     * @param doctorIdList
     * @return
     */
    List<HmcDoctorInfoDTO> getDoctorInfoByIds(List<Integer> doctorIdList);

    /**
     * 医生是否开启问诊服务项
     *
     * @param doctorId
     * @return
     */
    Boolean checkDoctorDiagnosis(Integer doctorId);

    /**
     * 分页查询医生
     *
     * @param request
     * @return
     */
    Page<HmcDoctorInfoDTO> queryDoctorPage(QueryDoctorPageRequest request);

    /**
     * 查询推荐医生
     * @param departmentIds
     * @return
     */
    List<RecommendDoctorDTO> queryRecommendDoctor(List<Integer> departmentIds);

    /**
     * 查看用户是否和医生绑定-医带患
     * @param request
     * @return
     */
    Boolean checkUserDoctorRel(CheckUserDoctorRelRequest request);

    /**
     * 查看活动页访问人数、参与医生数量、患者数量-医带患
     * @param request
     * @return
     */
    List<ActivityDocPatientCountDTO> getActivityDoctorPatientCount(GetActivityDoctorPatientCountRequest request);

    /**
     * 医带患活动医生分页
     * @param request
     * @return
     */
    Page<HmcActivityDocPatientDTO> queryActivityDocPatientPage(QueryActivityDocPatientListRequest request);

    /**
     * 医带患活动患者详情
     * @param id
     * @return
     */
    HmcActivityDocPatientDetailDTO queryActivityDocPatientDetail(Integer id);

    /**
     * 患者审核
     * @param request
     * @return
     */
    Boolean activityDocPatientAudit(ActivityDocPatientAuditRequest request);

    /**
     * 导入活动医生
     * @param request
     */
    List<HmcActivityImportDocResDTO> importActivityDoc(List<HMCImportActivityDocRequest> request);

    /**
     * 查询医生信息
     * @param request
     * @return
     */
    List<Long> queryDoctorByIdAndName(QueryDoctorRequest request);
}
