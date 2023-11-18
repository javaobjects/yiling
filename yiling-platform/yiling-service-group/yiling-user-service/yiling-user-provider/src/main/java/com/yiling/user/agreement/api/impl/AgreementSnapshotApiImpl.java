package com.yiling.user.agreement.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.user.agreement.api.AgreementSnapshotApi;
import com.yiling.user.agreement.dto.AgreementSnapshotDTO;
import com.yiling.user.agreement.dto.SupplementAgreementDetailDTO;
import com.yiling.user.agreement.service.AgreementSnapshotService;

/**
 * @author: houjie.sun
 * @date: 2021/8/20
 */
@DubboService
public class AgreementSnapshotApiImpl implements AgreementSnapshotApi {

    @Autowired
    private AgreementSnapshotService agreementSnapshotService;

    @Override
    public AgreementSnapshotDTO queryAgreementSnapshotById(Long id) {
        return agreementSnapshotService.queryAgreementSnapshotById(id);
    }

    @Override
    public List<AgreementSnapshotDTO> queryAgreementSnapshotByAgreementId(Long agreementId) {
        return agreementSnapshotService.queryAgreementSnapshotByAgreementId(agreementId);
    }

    @Override
    public SupplementAgreementDetailDTO querySupplementAgreementsDetailById(Long id) {
        return agreementSnapshotService.querySupplementAgreementsDetailById(id);
    }
}
