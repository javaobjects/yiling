package com.yiling.user.agreementv2.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.user.agreementv2.entity.AgreementRebateControlAreaDetailDO;
import com.yiling.user.agreementv2.dao.AgreementRebateControlAreaDetailMapper;
import com.yiling.user.agreementv2.service.AgreementRebateControlAreaDetailService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议返利范围控销区域详情表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-03
 */
@Service
public class AgreementRebateControlAreaDetailServiceImpl extends BaseServiceImpl<AgreementRebateControlAreaDetailMapper, AgreementRebateControlAreaDetailDO> implements AgreementRebateControlAreaDetailService {

    @Override
    public List<AgreementRebateControlAreaDetailDO> getControlAreaDetailList(Long rebateScopeId) {
        LambdaQueryWrapper<AgreementRebateControlAreaDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementRebateControlAreaDetailDO::getRebateScopeId, rebateScopeId);
        return this.list(wrapper);
    }
}
