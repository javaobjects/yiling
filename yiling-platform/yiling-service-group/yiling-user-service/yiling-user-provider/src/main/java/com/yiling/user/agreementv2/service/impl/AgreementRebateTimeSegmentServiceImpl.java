package com.yiling.user.agreementv2.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementRebateTimeSegmentDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateTimeSegmentDO;
import com.yiling.user.agreementv2.dao.AgreementRebateTimeSegmentMapper;
import com.yiling.user.agreementv2.service.AgreementRebateTimeSegmentService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议时段表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Service
public class AgreementRebateTimeSegmentServiceImpl extends BaseServiceImpl<AgreementRebateTimeSegmentMapper, AgreementRebateTimeSegmentDO> implements AgreementRebateTimeSegmentService {

    @Override
    public List<AgreementRebateTimeSegmentDTO> getRebateTimeByAgreementId(Long agreementId) {
        LambdaQueryWrapper<AgreementRebateTimeSegmentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementRebateTimeSegmentDO::getAgreementId, agreementId);
        return PojoUtils.map(this.list(wrapper), AgreementRebateTimeSegmentDTO.class);
    }
}
