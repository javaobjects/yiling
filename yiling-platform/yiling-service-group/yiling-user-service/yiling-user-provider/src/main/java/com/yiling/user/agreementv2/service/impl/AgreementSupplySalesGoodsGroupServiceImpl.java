package com.yiling.user.agreementv2.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.user.agreementv2.entity.AgreementSupplySalesGoodsGroupDO;
import com.yiling.user.agreementv2.dao.AgreementSupplySalesGoodsGroupMapper;
import com.yiling.user.agreementv2.service.AgreementSupplySalesGoodsGroupService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议供销商品组表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-03-03
 */
@Service
public class AgreementSupplySalesGoodsGroupServiceImpl extends BaseServiceImpl<AgreementSupplySalesGoodsGroupMapper, AgreementSupplySalesGoodsGroupDO> implements AgreementSupplySalesGoodsGroupService {

    @Override
    public List<AgreementSupplySalesGoodsGroupDO> getByAgreementId(Long agreementId) {
        LambdaQueryWrapper<AgreementSupplySalesGoodsGroupDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementSupplySalesGoodsGroupDO::getAgreementId, agreementId);
        return this.list(wrapper);
    }
}
