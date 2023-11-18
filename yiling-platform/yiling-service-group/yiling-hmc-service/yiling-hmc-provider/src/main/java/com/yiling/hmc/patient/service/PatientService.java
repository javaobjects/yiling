package com.yiling.hmc.patient.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.patient.dto.PatientDTO;
import com.yiling.hmc.patient.dto.request.QueryPatientPageRequest;
import com.yiling.hmc.patient.dto.request.SavePatientRequest;
import com.yiling.hmc.patient.entity.PatientDO;

/**
 * <p>
 * 就诊人 服务类
 * </p>
 *
 * @author gxl
 * @date 2022-04-07
 */
public interface PatientService extends BaseService<PatientDO> {

    /**
     * 保存患者信息
     * @param request
     */
    void savePatient(SavePatientRequest request);

    /**
     * 分页列表
     * @param request
     * @return
     */
    Page<PatientDTO> queryPatientPage(QueryPatientPageRequest request);

    /**
     * 根据userid查询关联就诊人数
     * @param userIdList
     * @return
     */
    Map<Long,Long> queryPatientCountByUserId(List<Long> userIdList);
}
