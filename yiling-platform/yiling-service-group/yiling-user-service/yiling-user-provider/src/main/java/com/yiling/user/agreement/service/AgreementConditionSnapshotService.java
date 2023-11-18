package com.yiling.user.agreement.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreement.entity.AgreementConditionSnapshotDO;

/**
 * <p>
 * 协议返利条件快照表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-09
 */
public interface AgreementConditionSnapshotService extends BaseService<AgreementConditionSnapshotDO> {

    /**
     * 通过协议快照Id获取所有的条件快照列表
     * @param agreementId
     * @param version
     * @return
     */
    List<AgreementConditionSnapshotDO> getAgreementConditionSnapshotByAgreementId(Long agreementId, Integer version);
}
