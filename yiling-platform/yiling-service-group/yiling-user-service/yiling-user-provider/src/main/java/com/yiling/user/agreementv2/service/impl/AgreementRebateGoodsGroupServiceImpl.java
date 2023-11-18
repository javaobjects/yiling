package com.yiling.user.agreementv2.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.agreementv2.dto.AgreementRebateGoodsGroupDTO;
import com.yiling.user.agreementv2.entity.AgreementRebateGoodsGroupDO;
import com.yiling.user.agreementv2.dao.AgreementRebateGoodsGroupMapper;
import com.yiling.user.agreementv2.service.AgreementRebateGoodsGroupService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 协议返利商品组表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-02-24
 */
@Service
public class AgreementRebateGoodsGroupServiceImpl extends BaseServiceImpl<AgreementRebateGoodsGroupMapper, AgreementRebateGoodsGroupDO> implements AgreementRebateGoodsGroupService {

    @Override
    public List<AgreementRebateGoodsGroupDTO> getRebateGoodsGroupByAgreementId(Long agreementId) {
        LambdaQueryWrapper<AgreementRebateGoodsGroupDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementRebateGoodsGroupDO::getAgreementId, agreementId);
        return PojoUtils.map(this.list(wrapper), AgreementRebateGoodsGroupDTO.class);
    }

    @Override
    public List<AgreementRebateGoodsGroupDTO> getRebateGoodsGroupBySegmentId(Long segmentId) {
        LambdaQueryWrapper<AgreementRebateGoodsGroupDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgreementRebateGoodsGroupDO::getSegmentId, segmentId);
        return PojoUtils.map(this.list(wrapper), AgreementRebateGoodsGroupDTO.class);
    }
}
