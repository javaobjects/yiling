package com.yiling.sjms.agency.service;

import com.yiling.sjms.agency.dto.request.UpdateAgencyLockArchiveRequest;
import com.yiling.sjms.agency.entity.AgencyUnlockRelationShipDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 机构解锁三者关系 服务类
 * </p>
 *
 * @author handy
 * @date 2023-02-22
 */
public interface AgencyUnlockRelationShipService extends BaseService<AgencyUnlockRelationShipDO> {

    void updateArchiveStatusById(UpdateAgencyLockArchiveRequest request);
}
