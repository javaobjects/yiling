package com.yiling.user.agreement.api;

import java.util.List;

import com.yiling.user.agreement.dto.AgreementSnapshotDTO;
import com.yiling.user.agreement.dto.SupplementAgreementDetailDTO;

/**
 * @author: houjie.sun
 * @date: 2021/8/20
 */
public interface AgreementSnapshotApi {

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
