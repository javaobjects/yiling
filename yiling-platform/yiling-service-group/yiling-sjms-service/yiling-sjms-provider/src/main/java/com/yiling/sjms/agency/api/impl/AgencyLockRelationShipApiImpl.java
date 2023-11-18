package com.yiling.sjms.agency.api.impl;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.sjms.agency.api.AgencyLockRelationShipApi;
import com.yiling.sjms.agency.dto.AgencyLockRelationShipDTO;
import com.yiling.sjms.agency.service.AgencyLockRelationShipService;

/**
 * @author: dexi.yao
 * @date: 2023-02-24
 */
@DubboService
public class AgencyLockRelationShipApiImpl implements AgencyLockRelationShipApi {

    @Autowired
    AgencyLockRelationShipService reShipService;

    @Override
    public List<AgencyLockRelationShipDTO> listByForm(Long agencyFormId, Long crmEnterpriseId) {
        return reShipService.listByForm(agencyFormId,crmEnterpriseId);
    }

    @Override
    public Map<Long, List<AgencyLockRelationShipDTO>> queryRelationListByAgencyFormId(List<Long> agencyFormIdList) {
        return reShipService.queryRelationListByAgencyFormId(agencyFormIdList);
    }
}
