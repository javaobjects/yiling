package com.yiling.user.agreementv2.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.user.agreementv2.entity.AgreementPaymentMethodDO;
import com.yiling.user.agreementv2.dao.AgreementPaymentMethodMapper;
import com.yiling.user.agreementv2.service.AgreementPaymentMethodService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议付款方式表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Service
public class AgreementPaymentMethodServiceImpl extends BaseServiceImpl<AgreementPaymentMethodMapper, AgreementPaymentMethodDO> implements AgreementPaymentMethodService {

    @Override
    public List<AgreementPaymentMethodDO> queryList(Long agreementId) {
        LambdaQueryWrapper<AgreementPaymentMethodDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementPaymentMethodDO::getAgreementId, agreementId);
        return this.list(wrapper);
    }
}
