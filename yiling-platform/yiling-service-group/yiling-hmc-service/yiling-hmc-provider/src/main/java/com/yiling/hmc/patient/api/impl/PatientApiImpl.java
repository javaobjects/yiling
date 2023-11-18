package com.yiling.hmc.patient.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.hmc.patient.api.PatientApi;
import com.yiling.hmc.patient.dto.PatientDTO;
import com.yiling.hmc.patient.dto.request.QueryPatientPageRequest;
import com.yiling.hmc.patient.dto.request.SavePatientRequest;
import com.yiling.hmc.patient.service.PatientService;

/**
 * @author: gxl
 * @date: 2022/4/7
 */
@DubboService
public class PatientApiImpl implements PatientApi {

    @Autowired
    private PatientService patientService;

    @Override
    public Page<PatientDTO> queryPatientPage(QueryPatientPageRequest request) {
        return patientService.queryPatientPage(request);
    }

    @Override
    public void savePatient(SavePatientRequest request) {
        patientService.savePatient(request);
    }

    @Override
    public Map<Long, Long> queryPatientCountByUserId(List<Long> userIdList) {
        return patientService.queryPatientCountByUserId(userIdList);
    }
}