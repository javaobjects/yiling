package com.yiling.user.agreementv2.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementRebateProjectServiceRewardDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateProjectServiceRewardDO;
import com.yiling.user.agreementv2.dao.AgreementRebateProjectServiceRewardMapper;
import com.yiling.user.agreementv2.service.AgreementRebateProjectServiceRewardService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议返利-项目服务奖励阶梯表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-05-17
 */
@Service
public class AgreementRebateProjectServiceRewardServiceImpl extends BaseServiceImpl<AgreementRebateProjectServiceRewardMapper, AgreementRebateProjectServiceRewardDO> implements AgreementRebateProjectServiceRewardService {

    @Override
    public List<AgreementRebateProjectServiceRewardDTO> getProjectServiceRewardListByAgreementId(Long agreementId) {
        LambdaQueryWrapper<AgreementRebateProjectServiceRewardDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementRebateProjectServiceRewardDO::getAgreementId, agreementId);
        return PojoUtils.map(this.list(wrapper), AgreementRebateProjectServiceRewardDTO.class);
    }

}
