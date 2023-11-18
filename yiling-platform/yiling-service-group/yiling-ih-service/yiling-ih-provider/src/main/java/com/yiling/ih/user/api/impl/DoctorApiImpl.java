package com.yiling.ih.user.api.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cn.hutool.json.JSONUtil;
import com.beust.jcommander.internal.Lists;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.ih.patient.enums.IHErrorCode;
import com.yiling.ih.user.dto.*;
import com.yiling.ih.user.dto.request.*;
import com.yiling.ih.user.feign.dto.response.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.ih.common.ApiPage;
import com.yiling.ih.common.ApiResult;
import com.yiling.ih.user.api.DoctorApi;
import com.yiling.ih.user.feign.DoctorFeignClient;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;

/**
 * 医生 API 实现
 *
 * @author: xuan.zhou
 * @date: 2022/6/8
 */
@DubboService
@Slf4j
public class DoctorApiImpl implements DoctorApi {

    @Autowired
    private DoctorFeignClient doctorFeignClient;

    @Override
    public List<DoctorInfoDTO> listByMrId(QueryMrDoctorListRequest request) {
        ApiResult<List<GetDoctorListByAgentIdResponse>> result = doctorFeignClient.getDoctorListByAgentId(request.getMrId(), request.getDoctorName());
        if (result.success()) {
            List<GetDoctorListByAgentIdResponse> data = result.getData();
            if (CollUtil.isEmpty(data)) {
                return ListUtil.empty();
            }

            List<DoctorInfoDTO> list = data.stream().map(e -> this.convert(e)).collect(Collectors.toList());
            return list;
        }

        return ListUtil.empty();
    }

    private DoctorInfoDTO convert(GetDoctorListByAgentIdResponse response) {
        DoctorInfoDTO doctorInfoDTO = new DoctorInfoDTO();
        doctorInfoDTO.setId(response.getId());
        doctorInfoDTO.setName(response.getDoctorName());
        doctorInfoDTO.setJobTitle(response.getProfession());
        doctorInfoDTO.setMobile(response.getCellularphone());
        doctorInfoDTO.setIdNumber(response.getNumber());
        doctorInfoDTO.setOrgName(response.getHospitalName());
        doctorInfoDTO.setDeptName(response.getHospitalDepartment());
        doctorInfoDTO.setStatus(response.getState());
        return doctorInfoDTO;
    }

    @Override
    public Page<DoctorListAppInfoDTO> listAppByMrId(QueryAppMrDoctorListRequest request) {
        ApiResult<ApiPage<DoctorListAppInfoDTO>> result = doctorFeignClient.getDoctorListAppByAgentId(request);
        Page<DoctorListAppInfoDTO> doctorAppInfoDTOPage = request.getPage();
        if (result.success()) {
            ApiPage<DoctorListAppInfoDTO> data = result.getData();
            doctorAppInfoDTOPage.setRecords(data.getList()).setTotal(data.getTotal());
        }
        return doctorAppInfoDTOPage;
    }

    @Override
    public DoctorAppInfoDTO getDoctorInfoByDoctorId(Integer doctorId) {
        ApiResult<GetDoctorInfoByDoctorIdResponse> result = doctorFeignClient.getDoctorInfoByDoctorId(doctorId);
        if (result.success() && result.getData() != null) {
            GetDoctorInfoByDoctorIdResponse response = result.getData();
            DoctorAppInfoDTO doctorAppInfoDTO = convertDoctorInfo(response);
            return doctorAppInfoDTO;
        }
        return null;
    }

    private DoctorAppInfoDTO convertDoctorInfo(GetDoctorInfoByDoctorIdResponse response) {
        DoctorAppInfoDTO doctorAppInfoDTO = new DoctorAppInfoDTO();
        doctorAppInfoDTO.setDoctorId(response.getDoctorId());
        doctorAppInfoDTO.setDoctorName(response.getDoctorName());
        doctorAppInfoDTO.setPicture(response.getPicture());
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

    @Override
    public Page<HmcDoctorListDTO> queryDoctorList(QueryDoctorListRequest request) {
        if (Objects.nonNull(request.getEndTime())) {
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }
        ApiResult<ApiPage<HmcDoctorListResponse>> result = doctorFeignClient.queryDoctorList(request);
        ApiPage<HmcDoctorListResponse> hmcDoctorListDTOPage = result.getData();
        Page<HmcDoctorListDTO> page = request.getPage();
        if (result.success() && hmcDoctorListDTOPage.getTotal() > 0) {
            List<HmcDoctorListResponse> hmcDoctorListDTOS = hmcDoctorListDTOPage.getList();
            List<HmcDoctorListDTO> list = hmcDoctorListDTOS.stream().map(e -> this.convertDoctor(e)).collect(Collectors.toList());
            page.setRecords(list).setTotal(hmcDoctorListDTOPage.getTotal());
        }
        return page;
    }

    @Override
    public Page<HmcDoctorListDTO> activityQueryDoctorList(QueryDoctorListRequest request) {
        if (Objects.nonNull(request.getEndTime())) {
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }
        ApiResult<ApiPage<HmcDoctorListResponse>> result = doctorFeignClient.activityQueryDoctorList(request);
        ApiPage<HmcDoctorListResponse> hmcDoctorListDTOPage = result.getData();
        Page<HmcDoctorListDTO> page = request.getPage();
        if (result.success() && hmcDoctorListDTOPage.getTotal() > 0) {
            List<HmcDoctorListResponse> hmcDoctorListDTOS = hmcDoctorListDTOPage.getList();
            List<HmcDoctorListDTO> list = hmcDoctorListDTOS.stream().map(e -> this.convertDoctor(e)).collect(Collectors.toList());
            page.setRecords(list).setTotal(hmcDoctorListDTOPage.getTotal());
        }
        return page;
    }

    @Override
    public Page<HmcDoctorListDTO> activityBaZiQueryDocPage(QueryDoctorListRequest request) {
        if (Objects.nonNull(request.getEndTime())) {
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }
        ApiResult<ApiPage<HmcDoctorListResponse>> result = doctorFeignClient.activityBaZiQueryDoctorList(request);
        ApiPage<HmcDoctorListResponse> hmcDoctorListDTOPage = result.getData();
        Page<HmcDoctorListDTO> page = request.getPage();
        if (result.success() && hmcDoctorListDTOPage.getTotal() > 0) {
            List<HmcDoctorListResponse> hmcDoctorListDTOS = hmcDoctorListDTOPage.getList();
            List<HmcDoctorListDTO> list = hmcDoctorListDTOS.stream().map(e -> this.convertDoctor(e)).collect(Collectors.toList());
            page.setRecords(list).setTotal(hmcDoctorListDTOPage.getTotal());
        }
        return page;
    }

    private HmcDoctorListDTO convertDoctor(HmcDoctorListResponse response) {
        HmcDoctorListDTO hmcDoctorListDTO = new HmcDoctorListDTO();
        hmcDoctorListDTO.setCreateTime(response.getCreateTime()).setDoctorId(response.getDoctorId()).setHospitalName(response.getHospitalName())
                .setName(response.getName()).setSource(response.getSource()).setActivityState(response.getActivityState());
        return hmcDoctorListDTO;
    }

    @Override
    public Page<HmcActivityDoctorDTO> queryActivityDoctorList(QueryActivityDoctorListRequest request) {
        ApiResult<ApiPage<HmcActivityDoctorDTO>> result = doctorFeignClient.queryActivityDoctorList(request);
        Page<HmcActivityDoctorDTO> page = request.getPage();
        ApiPage<HmcActivityDoctorDTO> hmcActivityDoctorDTOApiPage = result.getData();
        if (result.success() && hmcActivityDoctorDTOApiPage.getTotal() > 0) {
            page.setRecords(hmcActivityDoctorDTOApiPage.getList()).setTotal(hmcActivityDoctorDTOApiPage.getTotal());
        }
        log.info("[queryActivityDoctorList]page：{}", JSONUtil.toJsonStr(page));
        return page;
    }

    @Override
    public Page<HmcActivityDocDTO> queryActivityDocPage(QueryActivityDoctorListRequest request) {
        ApiResult<ApiPage<HmcActivityDocDTO>> result = doctorFeignClient.queryActivityDocPage(request);
        Page<HmcActivityDocDTO> page = request.getPage();
        ApiPage<HmcActivityDocDTO> hmcActivityDoctorDTOApiPage = result.getData();
        if (result.success() && hmcActivityDoctorDTOApiPage.getTotal() > 0) {
            page.setRecords(hmcActivityDoctorDTOApiPage.getList()).setTotal(hmcActivityDoctorDTOApiPage.getTotal());
        }
        log.info("[queryActivityDocPage]page：{}", JSONUtil.toJsonStr(page));
        return page;
    }

    @Override
    public Page<HmcActivityDocDTO> queryActivityBaZiPage(QueryActivityDoctorListRequest request) {
        ApiResult<ApiPage<HmcActivityDocDTO>> result = doctorFeignClient.queryActivityBaZiDoctorPage(request);
        Page<HmcActivityDocDTO> page = request.getPage();
        ApiPage<HmcActivityDocDTO> hmcActivityDoctorDTOApiPage = result.getData();
        if (result.success() && hmcActivityDoctorDTOApiPage.getTotal() > 0) {
            page.setRecords(hmcActivityDoctorDTOApiPage.getList()).setTotal(hmcActivityDoctorDTOApiPage.getTotal());
        }
        log.info("[queryActivityBaZiPage]page：{}", JSONUtil.toJsonStr(page));
        return page;
    }

    @Override
    public Boolean verifyActivityDoctor(VerifyActivityDoctorRequest request) {
        ApiResult<Boolean> result = doctorFeignClient.verifyActivityDoctor(request);
        log.info("[verifyActivityDoctor]result：{}", JSONUtil.toJsonStr(result));
        if (result.success() && result.getData()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public void saveActivityDoctor(SaveActivityDoctorRequest request) {
        ApiResult apiResult = doctorFeignClient.saveActivityDoctor(request);
        if (apiResult.success()) {
            //todo 异常信息返回
        }
    }

    @Override
    public void saveActivityDocPatientDoctor(SaveActivityDoctorRequest request) {
        ApiResult apiResult = doctorFeignClient.saveActivityDocPatientDoctor(request);
        if (!apiResult.success()) {
            log.error("保存医带患活动出错，{}", apiResult.getMsg());
            throw new BusinessException(IHErrorCode.ACTIVITY_DOC_PATIENT);
        }
    }

    @Override
    public void saveActivityBaZiDoctor(SaveActivityDoctorRequest request) {
        ApiResult apiResult = doctorFeignClient.saveActivityBaZiDoctor(request);
        if (!apiResult.success()) {
            log.error("保存医带患活动出错，{}", apiResult.getMsg());
            throw new BusinessException(IHErrorCode.ACTIVITY_DOC_PATIENT);
        }
    }

    @Override
    public void deleteActivityDoctor(DeleteActivityDoctorRequest request) {
        ApiResult apiResult = doctorFeignClient.deleteActivityDoctor(request);
        if (apiResult.success()) {

        }
    }

    @Override
    public void updateActivityDoctorStatus(DeleteActivityDoctorRequest request) {
        ApiResult apiResult = doctorFeignClient.updateActivityDoctorStatus(request);
        if (!apiResult.success()) {
            throw new BusinessException(IHErrorCode.UPDATE_ACTIVITY_DOCTOR_ERROR);
        }
    }

    @Override
    public void deleteActivityBaZiDoctorStatus(DeleteActivityDoctorRequest request) {
        ApiResult apiResult = doctorFeignClient.deleteBaZiActivityDoctor(request);
        if (!apiResult.success()) {
            throw new BusinessException(IHErrorCode.ACTIVITY_DOC_REMOVE_PATIENT);
        }
    }

    @Override
    public List<HmcVocationalHospitalDTO> queryVocationalHospitalList(String hospitalName) {
        ApiResult<List<HmcVocationalHospitalDTO>> apiResult = doctorFeignClient.queryVocationalHospitalList(hospitalName);
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return null;
    }

    @Override
    public List<Integer> checkActivityDoctor(Integer activityId, List<Integer> doctorIdList) {
        ApiResult<List<Integer>> apiResult = doctorFeignClient.checkActivityDoctor(activityId, doctorIdList);
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return null;
    }

    @Override
    public List<HmcDoctorInfoDTO> getDoctorInfoByIds(List<Integer> doctorIdList) {
        ApiResult<List<HmcDoctorInfoDTO>> apiResult = doctorFeignClient.getDoctorInfoByIds(doctorIdList);
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return Lists.newArrayList();
    }

    @Override
    public Boolean checkDoctorDiagnosis(Integer doctorId) {
        ApiResult<Boolean> apiResult = doctorFeignClient.checkDoctorDiagnosis(doctorId);
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return Boolean.FALSE;
    }

    @Override
    public Page<HmcDoctorInfoDTO> queryDoctorPage(QueryDoctorPageRequest request) {
        ApiResult<ApiPage<HmcDoctorInfoDTO>> result = doctorFeignClient.queryDoctorPage(request);
        Page<HmcDoctorInfoDTO> page = request.getPage();
        ApiPage<HmcDoctorInfoDTO> data = result.getData();
        if (result.success() && data.getTotal() > 0) {
            page.setRecords(data.getList()).setTotal(data.getTotal());
        }
        return page;
    }

    @Override
    public List<RecommendDoctorDTO> queryRecommendDoctor(List<Integer> departmentIds) {
        QueryRecommendDoctorRequest request = new QueryRecommendDoctorRequest();
        request.setDepartmentIds(departmentIds);
        ApiResult<List<RecommendDoctorDTO>> apiResult = doctorFeignClient.queryRecommendDoctor(request);
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return Lists.newArrayList();
    }

    @Override
    public Boolean checkUserDoctorRel(CheckUserDoctorRelRequest request) {
        ApiResult<Boolean> apiResult = doctorFeignClient.checkUserDoctorRel(request);
        if (apiResult.success()) {
            return apiResult.getData();
        }
        return Boolean.FALSE;
    }

    @Override
    public List<ActivityDocPatientCountDTO> getActivityDoctorPatientCount(GetActivityDoctorPatientCountRequest request) {
        ApiResult<List<ActivityDocPatientCountResponse>> apiResult = doctorFeignClient.getActivityDoctorPatientCount(request);
        if (apiResult.success()) {
            return PojoUtils.map(apiResult.getData(), ActivityDocPatientCountDTO.class);
        }
        return ListUtil.empty();
    }

    @Override
    public Page<HmcActivityDocPatientDTO> queryActivityDocPatientPage(QueryActivityDocPatientListRequest request) {
        ApiResult<ApiPage<ActivityDocPatientResponse>> result = doctorFeignClient.queryActivityDocPatientPage(request);
        Page<HmcActivityDocPatientDTO> page = request.getPage();
        ApiPage<ActivityDocPatientResponse> data = result.getData();
        if (result.success() && data.getTotal() > 0) {
            page.setRecords(PojoUtils.map(data.getList(), HmcActivityDocPatientDTO.class)).setTotal(data.getTotal());
        }
        log.info("[queryActivityDocPatientPage]：{}", JSONUtil.toJsonStr(page));
        return page;
    }

    @Override
    public HmcActivityDocPatientDetailDTO queryActivityDocPatientDetail(Integer id) {
        ApiResult<HmcActivityDocPatientDetailDTO> result = doctorFeignClient.queryActivityDocPatientDetail(id);
        if (!result.success()) {
            throw new BusinessException(IHErrorCode.ACTIVITY_GET_PATIENT_ERROR);
        }
        log.info("[queryActivityDocPatientDetail]：{}", JSONUtil.toJsonStr(result));
        return result.getData();
    }

    @Override
    public Boolean activityDocPatientAudit(ActivityDocPatientAuditRequest request) {
        ApiResult<Boolean> result = doctorFeignClient.activityDocPatientAudit(request);
        if (!result.success()) {
            throw new BusinessException(IHErrorCode.ACTIVITY_DOC_PATIENT_AUDIT_ERROR);
        }
        log.info("[queryActivityDocPatientAudit]：{}", JSONUtil.toJsonStr(result));
        return Boolean.TRUE;
    }

    @Override
    public List<HmcActivityImportDocResDTO> importActivityDoc(List<HMCImportActivityDocRequest> requestList) {
        ApiResult<List<HmcActivityImportDocResDTO>> result = doctorFeignClient.importActivityDoc(requestList);
        if (!result.success()) {
            throw new BusinessException(IHErrorCode.ACTIVITY_DOC_IMPORT_ERROR);
        }
        return result.getData();
    }

    @Override
    public List<Long> queryDoctorByIdAndName(QueryDoctorRequest request) {
        ApiResult<List<Long>> result = doctorFeignClient.queryDoctorByIdAndName(request);
        if (!result.success()) {
            throw new BusinessException(IHErrorCode.ACTIVITY_DOC_PATIENT_QUERY_ERROR);
        }
        return result.getData();
    }
}
