package com.yiling.user.agreementv2.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementRebateTaskStageDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateTaskStageDO;
import com.yiling.user.agreementv2.dao.AgreementRebateTaskStageMapper;
import com.yiling.user.agreementv2.service.AgreementRebateTaskStageService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议返利任务量阶梯表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Service
public class AgreementRebateTaskStageServiceImpl extends BaseServiceImpl<AgreementRebateTaskStageMapper, AgreementRebateTaskStageDO> implements AgreementRebateTaskStageService {

    @Override
    public List<AgreementRebateTaskStageDTO> getRebateTaskStageList(Long agreementId) {
        LambdaQueryWrapper<AgreementRebateTaskStageDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementRebateTaskStageDO::getAgreementId, agreementId);
        return PojoUtils.map(this.list(wrapper), AgreementRebateTaskStageDTO.class);
    }
}
