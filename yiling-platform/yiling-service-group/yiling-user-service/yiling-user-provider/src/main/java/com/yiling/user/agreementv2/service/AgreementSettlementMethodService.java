package com.yiling.user.agreementv2.service;

import com.yiling.user.agreementv2.entity.AgreementSettlementMethodDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议结算方式表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
public interface AgreementSettlementMethodService extends BaseService<AgreementSettlementMethodDO> {

    /**
     * 根据协议Id获取结算信息
     *
     * @param agreementId
     * @return
     */
    AgreementSettlementMethodDO getByAgreementId(Long agreementId);

}
