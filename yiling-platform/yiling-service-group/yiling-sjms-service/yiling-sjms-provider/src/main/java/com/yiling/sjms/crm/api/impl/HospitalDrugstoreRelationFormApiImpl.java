package com.yiling.sjms.crm.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.agency.dto.request.QueryHosDruRelPageListRequest;
import com.yiling.sjms.crm.api.HospitalDrugstoreRelationFormApi;
import com.yiling.sjms.crm.dto.HospitalDrugstoreRelationExtFormDTO;
import com.yiling.sjms.crm.dto.HospitalDrugstoreRelationFormDTO;
import com.yiling.sjms.crm.dto.request.DeleteHosDruRelFormAppendixRequest;
import com.yiling.sjms.crm.dto.request.DeleteHospitalDrugstoreRelFormRequest;
import com.yiling.sjms.crm.dto.request.SaveOrUpdateHosDruRelAppendixListRequest;
import com.yiling.sjms.crm.dto.request.SaveOrUpdateHospitalDrugstoreRelFormRequest;
import com.yiling.sjms.crm.dto.request.SubmitHosDruRelRequest;
import com.yiling.sjms.crm.service.HospitalDrugstoreRelationExtFormService;
import com.yiling.sjms.crm.service.HospitalDrugstoreRelationFormService;

/**
 * @author fucheng.bai
 * @date 2023/6/7
 */
@DubboService
public class HospitalDrugstoreRelationFormApiImpl implements HospitalDrugstoreRelationFormApi {

    @Autowired
    private HospitalDrugstoreRelationFormService hospitalDrugstoreRelationFormService;

    @Autowired
    private HospitalDrugstoreRelationExtFormService hospitalDrugstoreRelationExtFormService;

    @Override
    public Long saveHospitalDrugstoreRelationForm(SaveOrUpdateHospitalDrugstoreRelFormRequest request) {
        return hospitalDrugstoreRelationFormService.saveHospitalDrugstoreRelationForm(request);
    }

    @Override
    public void removeById(DeleteHospitalDrugstoreRelFormRequest request) {
        hospitalDrugstoreRelationFormService.removeById(request);
    }

    @Override
    public void removeAppendixById(DeleteHosDruRelFormAppendixRequest request) {
        hospitalDrugstoreRelationExtFormService.removeById(request);
    }

    @Override
    public Page<HospitalDrugstoreRelationFormDTO> pageList(QueryHosDruRelPageListRequest request) {
        return PojoUtils.map(hospitalDrugstoreRelationFormService.pageList(request), HospitalDrugstoreRelationFormDTO.class);
    }

    @Override
    public HospitalDrugstoreRelationFormDTO detail(Long id) {
        return PojoUtils.map(hospitalDrugstoreRelationFormService.getById(id), HospitalDrugstoreRelationFormDTO.class);
    }

    @Override
    public void submit(SubmitHosDruRelRequest request) {
        hospitalDrugstoreRelationFormService.submit(request);
    }

    @Override
    public Long saveParentAppendixForm(SaveOrUpdateHosDruRelAppendixListRequest request) {
        return hospitalDrugstoreRelationFormService.saveParentAppendixForm(request);
    }

    @Override
    public HospitalDrugstoreRelationExtFormDTO queryAppendix(Long formId) {
        return PojoUtils.map(hospitalDrugstoreRelationExtFormService.detailByFormId(formId), HospitalDrugstoreRelationExtFormDTO.class);
    }

    @Override
    public List<Long> selectDrugOrgIdFormIdByHosOrgId(Long formId, Long hospitalOrgId) {
        return hospitalDrugstoreRelationFormService.selectDrugOrgIdFormIdByHosOrgId(formId, hospitalOrgId);
    }


}
