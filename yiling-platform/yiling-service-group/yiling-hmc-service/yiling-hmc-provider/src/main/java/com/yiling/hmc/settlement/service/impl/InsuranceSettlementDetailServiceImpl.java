package com.yiling.hmc.settlement.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.hmc.settlement.dao.InsuranceSettlementDetailMapper;
import com.yiling.hmc.settlement.entity.InsuranceSettlementDetailDO;
import com.yiling.hmc.settlement.service.InsuranceSettlementDetailService;

/**
 * <p>
 * 保司结账明细表 服务实现类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-05-13
 */
@Service
public class InsuranceSettlementDetailServiceImpl extends BaseServiceImpl<InsuranceSettlementDetailMapper, InsuranceSettlementDetailDO> implements InsuranceSettlementDetailService {

    @Override
    public List<InsuranceSettlementDetailDO> listByInsuranceSettlementId(Long insuranceSettlementId) {
        QueryWrapper<InsuranceSettlementDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(InsuranceSettlementDetailDO::getInsuranceSettlementId, insuranceSettlementId);
        return this.list(wrapper);
    }

    @Override
    public List<InsuranceSettlementDetailDO> listByOrderNoList(List<String> orderNoList) {
        QueryWrapper<InsuranceSettlementDetailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(InsuranceSettlementDetailDO::getOrderNo, orderNoList);
        return this.list(wrapper);
    }
}
