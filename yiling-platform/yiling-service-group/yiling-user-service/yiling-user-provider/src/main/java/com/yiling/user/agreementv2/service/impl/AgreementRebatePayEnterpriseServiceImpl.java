package com.yiling.user.agreementv2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dao.AgreementRebatePayEnterpriseMapper;
import com.yiling.user.agreementv2.dao.AgreementSupplySalesEnterpriseMapper;
import com.yiling.user.agreementv2.dto.AgreementRebatePayEnterpriseDTO;
import com.yiling.user.agreementv2.entity.AgreementRebatePayEnterpriseDO;
import com.yiling.user.agreementv2.entity.AgreementSupplySalesEnterpriseDO;
import com.yiling.user.agreementv2.service.AgreementRebatePayEnterpriseService;
import com.yiling.user.agreementv2.service.AgreementSupplySalesEnterpriseService;

/**
 * <p>
 * 协议返利支付方指定商业公司表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-28
 */
@Service
public class AgreementRebatePayEnterpriseServiceImpl extends BaseServiceImpl<AgreementRebatePayEnterpriseMapper, AgreementRebatePayEnterpriseDO> implements AgreementRebatePayEnterpriseService {

    @Override
    public List<AgreementRebatePayEnterpriseDTO> getRebatePayEnterpriseList(Long agreementId) {
        LambdaQueryWrapper<AgreementRebatePayEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementRebatePayEnterpriseDO::getAgreementId, agreementId);
        return PojoUtils.map(this.list(wrapper), AgreementRebatePayEnterpriseDTO.class);
    }
}
