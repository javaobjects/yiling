package com.yiling.dataflow.crm.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmHospitalDrugstoreRelationApi;
import com.yiling.dataflow.crm.bo.CrmHosDruRelOrgIdBO;
import com.yiling.dataflow.crm.dto.CrmHospitalDrugstoreRelationDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmHosDruRelEffectiveListRequest;
import com.yiling.dataflow.crm.dto.request.QueryHospitalDrugstoreRelationPageRequest;
import com.yiling.dataflow.crm.dto.request.RemoveHospitalDrugstoreRelRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmHospitalDrugstoreRelRequest;
import com.yiling.dataflow.crm.service.CrmHospitalDrugstoreRelationService;
import com.yiling.framework.common.util.PojoUtils;

/**
 * @author fucheng.bai
 * @date 2023/5/30
 */
@DubboService
public class CrmHospitalDrugstoreRelationApiImpl implements CrmHospitalDrugstoreRelationApi {

    @Autowired
    private CrmHospitalDrugstoreRelationService crmHospitalDrugstoreRelationService;

    @Override
    public Page<CrmHospitalDrugstoreRelationDTO> listPage(QueryHospitalDrugstoreRelationPageRequest request) {
        return PojoUtils.map(crmHospitalDrugstoreRelationService.listPage(request), CrmHospitalDrugstoreRelationDTO.class);
    }

    @Override
    public void saveOrUpdate(SaveOrUpdateCrmHospitalDrugstoreRelRequest request) {
        crmHospitalDrugstoreRelationService.saveOrUpdate(request);
    }

    @Override
    public List<CrmHosDruRelOrgIdBO> listGroupByHospitalIdAndDrugstoreId() {
        return crmHospitalDrugstoreRelationService.listGroupByHospitalIdAndDrugstoreId();
    }

    @Override
    public List<Long> selectDrugOrgIdByHosOrgId(Long hospitalOrgId) {
        return crmHospitalDrugstoreRelationService.selectDrugOrgIdByHosOrgId(hospitalOrgId);
    }

    @Override
    public void remove(RemoveHospitalDrugstoreRelRequest request) {
        crmHospitalDrugstoreRelationService.delete(request);
    }

    @Override
    public void disable(SaveOrUpdateCrmHospitalDrugstoreRelRequest request) {
        crmHospitalDrugstoreRelationService.disable(request);
    }

    @Override
    public List<CrmHospitalDrugstoreRelationDTO> getEffectiveList(QueryCrmHosDruRelEffectiveListRequest request) {
        return PojoUtils.map(crmHospitalDrugstoreRelationService.getEffectiveList(request), CrmHospitalDrugstoreRelationDTO.class);
    }

}
