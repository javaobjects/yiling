package com.yiling.user.agreement.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.user.agreement.dto.AgreementSnapshotDTO;
import com.yiling.user.agreement.dto.SupplementAgreementDetailDTO;
import com.yiling.user.agreement.entity.AgreementSnapshotDO;

/**
 * <p>
 * 协议快照表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2021-07-09
 */
public interface AgreementSnapshotService extends BaseService<AgreementSnapshotDO> {

    /**
     * 根据id查询协议快照详情
     * @param id
     */
    AgreementSnapshotDTO queryAgreementSnapshotById(Long id);

    /**
     * 根据协议id查询协议快照列表
     * @param agreementId
     */
    List<AgreementSnapshotDTO> queryAgreementSnapshotByAgreementId(Long agreementId);

    /**
     * 根据快照id查询补充协议详情
     *
     * @param id
     * @return
     */
    SupplementAgreementDetailDTO querySupplementAgreementsDetailById(Long id);

}
