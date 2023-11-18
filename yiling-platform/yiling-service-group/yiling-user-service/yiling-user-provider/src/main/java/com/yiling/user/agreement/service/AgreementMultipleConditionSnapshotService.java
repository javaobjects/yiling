package com.yiling.user.agreement.service;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreement.entity.AgreementMultipleConditionSnapshotDO;

/**
 * <p>
 * 协议多选条件表快照表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-09
 */
public interface AgreementMultipleConditionSnapshotService extends BaseService<AgreementMultipleConditionSnapshotDO> {

    /**
     * 查询某一个协议下面的所有多选
     * @param agreementId
     * @return
     */
    Map<String, List<Integer>> getConditionValueByAgreementId(Long agreementId, Integer version);
}
