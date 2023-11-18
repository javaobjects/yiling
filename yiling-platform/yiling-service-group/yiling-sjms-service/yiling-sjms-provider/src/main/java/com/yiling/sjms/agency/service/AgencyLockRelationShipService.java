package com.yiling.sjms.agency.service;

import java.util.List;
import java.util.Map;

import com.yiling.sjms.agency.dto.AgencyLockRelationShipDTO;
import com.yiling.sjms.agency.entity.AgencyLockRelationShipDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 机构锁定三者关系表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2023-02-23
 */
public interface AgencyLockRelationShipService extends BaseService<AgencyLockRelationShipDO> {

    /**
     * 根据锁定机构表单id和crmEntId查询表单申请的三者关系
     *
     * @param agencyFormId
     * @param crmEnterpriseId
     * @return
     */
    List<AgencyLockRelationShipDTO> listByForm(Long agencyFormId,Long crmEnterpriseId);

    /**
     * 根据机构锁定表的主键查询该主键对应的三者关系
     *
     * @param agencyFormIdList
     * @return
     */
    Map<Long,List<AgencyLockRelationShipDTO>> queryRelationListByAgencyFormId(List<Long> agencyFormIdList);

}
