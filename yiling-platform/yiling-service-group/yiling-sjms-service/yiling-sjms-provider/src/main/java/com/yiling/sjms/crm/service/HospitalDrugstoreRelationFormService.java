package com.yiling.sjms.crm.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.sjms.agency.dto.request.QueryHosDruRelPageListRequest;
import com.yiling.sjms.crm.dto.request.DeleteHospitalDrugstoreRelFormRequest;
import com.yiling.sjms.crm.dto.request.SaveOrUpdateHosDruRelAppendixListRequest;
import com.yiling.sjms.crm.dto.request.SaveOrUpdateHospitalDrugstoreRelFormRequest;
import com.yiling.sjms.crm.dto.request.SubmitHosDruRelRequest;
import com.yiling.sjms.crm.entity.HospitalDrugstoreRelationFormDO;

/**
 * <p>
 * 院外药店关系流程表单明细表 服务类
 * </p>
 *
 * @author fucheng.bai
 * @date 2023-06-07
 */
public interface HospitalDrugstoreRelationFormService extends BaseService<HospitalDrugstoreRelationFormDO> {

    Long saveHospitalDrugstoreRelationForm(SaveOrUpdateHospitalDrugstoreRelFormRequest request);

    void removeById(DeleteHospitalDrugstoreRelFormRequest request);

    Page<HospitalDrugstoreRelationFormDO> pageList(QueryHosDruRelPageListRequest request);

    boolean submit(SubmitHosDruRelRequest request);

    Long saveParentAppendixForm(SaveOrUpdateHosDruRelAppendixListRequest request);

    List<Long> selectDrugOrgIdFormIdByHosOrgId(Long formId, Long hospitalOrgId);

    boolean approveToAdd(Long formId);
}
