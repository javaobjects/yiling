package com.yiling.user.agreementv2.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementRebateBasicServiceRewardDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateBasicServiceRewardDO;
import com.yiling.user.agreementv2.dao.AgreementRebateBasicServiceRewardMapper;
import com.yiling.user.agreementv2.service.AgreementRebateBasicServiceRewardService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议返利-基础服务奖励阶梯表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-17
 */
@Service
public class AgreementRebateBasicServiceRewardServiceImpl extends BaseServiceImpl<AgreementRebateBasicServiceRewardMapper, AgreementRebateBasicServiceRewardDO> implements AgreementRebateBasicServiceRewardService {

    @Override
    public List<AgreementRebateBasicServiceRewardDTO> getBasicServiceRewardListByAgreementId(Long agreementId) {
        LambdaQueryWrapper<AgreementRebateBasicServiceRewardDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementRebateBasicServiceRewardDO::getAgreementId, agreementId);
        return PojoUtils.map(this.list(wrapper), AgreementRebateBasicServiceRewardDTO.class);
    }
}
