package com.yiling.dataflow.agency.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.CrmHospitalDTO;
import com.yiling.dataflow.agency.dto.request.QueryCrmAgencyPageListRequest;
import com.yiling.dataflow.agency.dto.request.SaveCrmHosptialRequest;
import com.yiling.dataflow.agency.dto.request.UpdateCrmHospitalRequest;
import com.yiling.dataflow.agency.dto.request.UpdateCrmSupplierRequest;
import com.yiling.dataflow.crm.dto.CrmEnterpriseRelationShipDTO;

/**
 * 零售机构档案扩展表 API
 *
 * @author: shixing.sun
 * @date: 2023/2/14 0014
 */
public interface CrmHosptialApi {
    Page<CrmHospitalDTO> pageList(QueryCrmAgencyPageListRequest request);

    List<CrmHospitalDTO> getHosptialInfoByCrmEnterId(List<Long> crmEnterIds);

    Boolean saveEnterpriseInfoAndHosptialInfo(SaveCrmHosptialRequest request);

    CrmHospitalDTO getCrmSupplierByCrmEnterId(String id);

    void removeCrmSupplierById(String id);

    List<CrmEnterpriseRelationShipDTO> getRelationByCrmEnterpriseId(Long id, String name);

    /**
     * 批量更新扩展信息
     *
     * @param requests
     * @return
     */
    boolean updateCrmHospitalBatch(List<UpdateCrmHospitalRequest> requests);
}
