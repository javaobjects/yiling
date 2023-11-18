package com.yiling.user.agreementv2.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementOtherRebateDTO;
import com.yiling.user.agreementv2.entity.AgreementOtherRebateDO;
import com.yiling.user.agreementv2.dao.AgreementOtherRebateMapper;
import com.yiling.user.agreementv2.service.AgreementOtherRebateService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 非商品返利表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Service
public class AgreementOtherRebateServiceImpl extends BaseServiceImpl<AgreementOtherRebateMapper, AgreementOtherRebateDO> implements AgreementOtherRebateService {

    @Override
    public List<AgreementOtherRebateDTO> getOtherRebateList(Long agreementId) {
        LambdaQueryWrapper<AgreementOtherRebateDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementOtherRebateDO::getAgreementId, agreementId);
        return PojoUtils.map(this.list(wrapper), AgreementOtherRebateDTO.class);
    }
}
