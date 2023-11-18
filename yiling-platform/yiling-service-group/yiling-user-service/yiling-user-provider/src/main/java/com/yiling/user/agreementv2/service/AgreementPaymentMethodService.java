package com.yiling.user.agreementv2.service;

import java.util.List;

import com.yiling.user.agreementv2.entity.AgreementPaymentMethodDO;
import com.yiling.framework.common.base.BaseService;

/**
 * <p>
 * 协议付款方式表 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
public interface AgreementPaymentMethodService extends BaseService<AgreementPaymentMethodDO> {

    /**
     * 查询协议支付方式
     *
     * @param agreementId
     * @return
     */
    List<AgreementPaymentMethodDO> queryList(Long agreementId);

}
