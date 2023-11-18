package com.yiling.user.agreementv2.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementRebateScopeDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateScopeDO;
import com.yiling.user.agreementv2.dao.AgreementRebateScopeMapper;
import com.yiling.user.agreementv2.service.AgreementRebateScopeService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议返利范围表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-03
 */
@Service
public class AgreementRebateScopeServiceImpl extends BaseServiceImpl<AgreementRebateScopeMapper, AgreementRebateScopeDO> implements AgreementRebateScopeService {

    @Override
    public List<AgreementRebateScopeDTO> getRebateScopeList(Long groupId) {
        LambdaQueryWrapper<AgreementRebateScopeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementRebateScopeDO::getGroupId, groupId);
        return PojoUtils.map(this.list(wrapper), AgreementRebateScopeDTO.class);
    }

    @Override
    public List<AgreementRebateScopeDTO> getRebateScopeByAgreementId(Long agreementId) {
        LambdaQueryWrapper<AgreementRebateScopeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementRebateScopeDO::getAgreementId, agreementId);
        return PojoUtils.map(this.list(wrapper), AgreementRebateScopeDTO.class);
    }
}
