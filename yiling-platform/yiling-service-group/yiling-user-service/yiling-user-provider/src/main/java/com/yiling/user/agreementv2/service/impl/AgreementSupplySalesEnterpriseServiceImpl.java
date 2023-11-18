package com.yiling.user.agreementv2.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementSupplySalesEnterpriseDTO;
import com.yiling.user.agreementv2.entity.AgreementSupplySalesEnterpriseDO;
import com.yiling.user.agreementv2.dao.AgreementSupplySalesEnterpriseMapper;
import com.yiling.user.agreementv2.service.AgreementSupplySalesEnterpriseService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议供销指定商业公司表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Service
public class AgreementSupplySalesEnterpriseServiceImpl extends BaseServiceImpl<AgreementSupplySalesEnterpriseMapper, AgreementSupplySalesEnterpriseDO> implements AgreementSupplySalesEnterpriseService {

    @Override
    public List<AgreementSupplySalesEnterpriseDTO> getByAgreementId(Long agreementId) {
        LambdaQueryWrapper<AgreementSupplySalesEnterpriseDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementSupplySalesEnterpriseDO::getAgreementId, agreementId);
        return PojoUtils.map(this.list(wrapper), AgreementSupplySalesEnterpriseDTO.class);
    }
}
