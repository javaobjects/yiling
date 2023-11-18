package com.yiling.dataflow.wash.service.impl;

import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.dataflow.wash.dao.UnlockSaleCustomerRangeMapper;
import com.yiling.dataflow.wash.entity.UnlockSaleCustomerRangeDO;
import com.yiling.dataflow.wash.service.UnlockSaleCustomerRangeService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2023-04-25
 */
@Service
public class UnlockSaleCustomerRangeServiceImpl extends BaseServiceImpl<UnlockSaleCustomerRangeMapper, UnlockSaleCustomerRangeDO> implements UnlockSaleCustomerRangeService {

    @Override
    public UnlockSaleCustomerRangeDO getUnlockSaleCustomerRangeByRuleId(Long ruleId) {
        LambdaQueryWrapper<UnlockSaleCustomerRangeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockSaleCustomerRangeDO::getRuleId, ruleId);
        List<UnlockSaleCustomerRangeDO> list = baseMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<UnlockSaleCustomerRangeDO> getUnlockSaleCustomerRangeByRuleIds(List<Long> ruleIds) {
        if(CollUtil.isEmpty(ruleIds)){
            return ListUtil.empty();
        }
        LambdaQueryWrapper<UnlockSaleCustomerRangeDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UnlockSaleCustomerRangeDO::getRuleId, ruleIds);
        return baseMapper.selectList(wrapper);
    }
}
