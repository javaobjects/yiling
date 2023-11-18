package com.yiling.dataflow.wash.service.impl;

import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.dataflow.wash.dao.UnlockSaleDepartmentMapper;
import com.yiling.dataflow.wash.entity.UnlockSaleDepartmentDO;
import com.yiling.dataflow.wash.service.UnlockSaleDepartmentService;
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
public class UnlockSaleDepartmentServiceImpl extends BaseServiceImpl<UnlockSaleDepartmentMapper, UnlockSaleDepartmentDO> implements UnlockSaleDepartmentService {

    @Override
    public UnlockSaleDepartmentDO getUnlockSaleDepartmentByRuleId(long ruleId) {
        LambdaQueryWrapper<UnlockSaleDepartmentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockSaleDepartmentDO::getRuleId, ruleId);
        List<UnlockSaleDepartmentDO> list = baseMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<UnlockSaleDepartmentDO> getUnlockSaleDepartmentByRuleIds(List<Long> ruleIds) {
        if (CollUtil.isEmpty(ruleIds)) {
            return ListUtil.empty();
        }
        LambdaQueryWrapper<UnlockSaleDepartmentDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UnlockSaleDepartmentDO::getRuleId, ruleIds);
        return baseMapper.selectList(wrapper);
    }
}
