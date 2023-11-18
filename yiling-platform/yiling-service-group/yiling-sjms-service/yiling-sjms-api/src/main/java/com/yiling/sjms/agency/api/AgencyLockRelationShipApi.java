package com.yiling.sjms.agency.api;

import java.util.List;
import java.util.Map;

import com.yiling.sjms.agency.dto.AgencyLockRelationShipDTO;

/**
 * @author: dexi.yao
 * @date: 2023/2/24
 */
public interface AgencyLockRelationShipApi {

    /**
     * 根据锁定机构表单id和crmEntId查询表单申请的三者关系
     *
     * @param agencyFormId
     * @param crmEnterpriseId
     * @return
     */
    List<AgencyLockRelationShipDTO> listByForm(Long agencyFormId, Long crmEnterpriseId);

    /**
     * 根据机构锁定表的主键查询该主键对应的三者关系
     *
     * @param agencyFormIdList
     * @return
     */
    Map<Long,List<AgencyLockRelationShipDTO>> queryRelationListByAgencyFormId(List<Long> agencyFormIdList);
}
