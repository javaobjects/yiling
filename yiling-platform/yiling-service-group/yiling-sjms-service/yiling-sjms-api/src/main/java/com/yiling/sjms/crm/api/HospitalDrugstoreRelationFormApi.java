package com.yiling.sjms.crm.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.sjms.agency.dto.request.QueryHosDruRelPageListRequest;
import com.yiling.sjms.crm.dto.HospitalDrugstoreRelationExtFormDTO;
import com.yiling.sjms.crm.dto.HospitalDrugstoreRelationFormDTO;
import com.yiling.sjms.crm.dto.request.DeleteHosDruRelFormAppendixRequest;
import com.yiling.sjms.crm.dto.request.DeleteHospitalDrugstoreRelFormRequest;
import com.yiling.sjms.crm.dto.request.SaveOrUpdateHosDruRelAppendixListRequest;
import com.yiling.sjms.crm.dto.request.SaveOrUpdateHospitalDrugstoreRelFormRequest;
import com.yiling.sjms.crm.dto.request.SubmitHosDruRelRequest;

/**
 * @author fucheng.bai
 * @date 2023/6/7
 */
public interface HospitalDrugstoreRelationFormApi {
    Long saveHospitalDrugstoreRelationForm(SaveOrUpdateHospitalDrugstoreRelFormRequest request);


    void removeById(DeleteHospitalDrugstoreRelFormRequest request);

    void removeAppendixById(DeleteHosDruRelFormAppendixRequest request);

    Page<HospitalDrugstoreRelationFormDTO> pageList(QueryHosDruRelPageListRequest request);

    HospitalDrugstoreRelationFormDTO detail(Long id);

    void submit(SubmitHosDruRelRequest request);

    Long saveParentAppendixForm(SaveOrUpdateHosDruRelAppendixListRequest request);

    HospitalDrugstoreRelationExtFormDTO queryAppendix(Long formId);

    List<Long> selectDrugOrgIdFormIdByHosOrgId(Long formId, Long hospitalOrgId);
}
