package com.yiling.dataflow.crm.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.bo.CrmHosDruRelOrgIdBO;
import com.yiling.dataflow.crm.dto.CrmHospitalDrugstoreRelationDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmHosDruRelEffectiveListRequest;
import com.yiling.dataflow.crm.dto.request.QueryHospitalDrugstoreRelationPageRequest;
import com.yiling.dataflow.crm.dto.request.RemoveHospitalDrugstoreRelRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmHospitalDrugstoreRelRequest;

/**
 * @author fucheng.bai
 * @date 2023/5/30
 */
public interface CrmHospitalDrugstoreRelationApi {

    Page<CrmHospitalDrugstoreRelationDTO> listPage(QueryHospitalDrugstoreRelationPageRequest request);

    void saveOrUpdate(SaveOrUpdateCrmHospitalDrugstoreRelRequest request);

    List<CrmHosDruRelOrgIdBO> listGroupByHospitalIdAndDrugstoreId();

    List<Long> selectDrugOrgIdByHosOrgId(Long hospitalOrgId);

    void remove(RemoveHospitalDrugstoreRelRequest request);

    void disable(SaveOrUpdateCrmHospitalDrugstoreRelRequest request);

    List<CrmHospitalDrugstoreRelationDTO> getEffectiveList(QueryCrmHosDruRelEffectiveListRequest request);
}
