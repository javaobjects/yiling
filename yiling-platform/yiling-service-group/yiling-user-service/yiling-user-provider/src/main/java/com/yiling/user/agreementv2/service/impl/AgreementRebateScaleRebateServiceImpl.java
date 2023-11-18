package com.yiling.user.agreementv2.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementRebateScaleRebateDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateScaleRebateDO;
import com.yiling.user.agreementv2.dao.AgreementRebateScaleRebateMapper;
import com.yiling.user.agreementv2.service.AgreementRebateScaleRebateService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议返利-规模返利阶梯表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-16
 */
@Service
public class AgreementRebateScaleRebateServiceImpl extends BaseServiceImpl<AgreementRebateScaleRebateMapper, AgreementRebateScaleRebateDO> implements AgreementRebateScaleRebateService {

    @Override
    public List<AgreementRebateScaleRebateDTO> getScaleRebateListByAgreementId(Long agreementId) {
        LambdaQueryWrapper<AgreementRebateScaleRebateDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementRebateScaleRebateDO::getAgreementId, agreementId);
        return PojoUtils.map(this.list(wrapper), AgreementRebateScaleRebateDTO.class);
    }
}
