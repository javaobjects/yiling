package com.yiling.hmc.patient.api;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.patient.dto.PatientDTO;
import com.yiling.hmc.patient.dto.request.QueryPatientPageRequest;
import com.yiling.hmc.patient.dto.request.SavePatientRequest;

/**
 * @author: gxl
 * @date: 2022/4/7
 */
public interface PatientApi {

    /**
     * 分页列表
     * @param request
     * @return
     */
    Page<PatientDTO> queryPatientPage(QueryPatientPageRequest request);

    /**
     * 保存患者信息
     * @param request
     */
    void savePatient(SavePatientRequest request);

    /**
     * 查询关联就诊人数
     * @param userIdList
     * @return
     */
    Map<Long, Long> queryPatientCountByUserId(List<Long> userIdList);
}