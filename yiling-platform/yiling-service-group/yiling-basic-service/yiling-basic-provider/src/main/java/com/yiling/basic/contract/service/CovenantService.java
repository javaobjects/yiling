package com.yiling.basic.contract.service;

import com.yiling.basic.contract.entity.CovenantDO;
import com.yiling.framework.common.base.BaseService;

/**
 * @author fucheng.bai
 * @date 2022/11/11
 */
public interface CovenantService extends BaseService<CovenantDO> {

    CovenantDO getByQysContractId(Long qysContractId);

    void updateStatus(Long id, String status);

    void updateFileKey(Long id, String fileKey);
}
