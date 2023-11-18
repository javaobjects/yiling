package com.yiling.user.agreementv2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.user.agreementv2.entity.AgreementSettlementMethodDO;
import com.yiling.user.agreementv2.dao.AgreementSettlementMethodMapper;
import com.yiling.user.agreementv2.service.AgreementSettlementMethodService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议结算方式表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Service
public class AgreementSettlementMethodServiceImpl extends BaseServiceImpl<AgreementSettlementMethodMapper, AgreementSettlementMethodDO> implements AgreementSettlementMethodService {

    @Override
    public AgreementSettlementMethodDO getByAgreementId(Long agreementId) {
        LambdaQueryWrapper<AgreementSettlementMethodDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementSettlementMethodDO::getAgreementId, agreementId);
        return this.getOne(wrapper);
    }
}
