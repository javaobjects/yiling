package com.yiling.dataflow.wash.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.dataflow.wash.dao.UnlockCustomerMatchingTaskMapper;
import com.yiling.dataflow.wash.entity.UnlockCustomerMatchingTaskDO;
import com.yiling.dataflow.wash.service.UnlockCustomerMatchingTaskService;
import com.yiling.framework.common.base.BaseServiceImpl;

import cn.hutool.core.collection.CollUtil;

/**
 * @author fucheng.bai
 * @date 2023/4/27
 */
@Service
public class UnlockCustomerMatchingTaskServiceImpl extends BaseServiceImpl<UnlockCustomerMatchingTaskMapper, UnlockCustomerMatchingTaskDO> implements UnlockCustomerMatchingTaskService {

    @Override
    public UnlockCustomerMatchingTaskDO getByCustomerName(String name) {
        LambdaQueryWrapper<UnlockCustomerMatchingTaskDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UnlockCustomerMatchingTaskDO::getCustomerName, name);
        wrapper.orderByDesc(UnlockCustomerMatchingTaskDO::getUpdateTime);
        List<UnlockCustomerMatchingTaskDO> list = baseMapper.selectList(wrapper);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<UnlockCustomerMatchingTaskDO> getByCustomerNameList(List<String> nameList) {
        if (CollUtil.isEmpty(nameList)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<UnlockCustomerMatchingTaskDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UnlockCustomerMatchingTaskDO::getCustomerName, nameList);
        wrapper.orderByDesc(UnlockCustomerMatchingTaskDO::getUpdateTime);
        List<UnlockCustomerMatchingTaskDO> list = baseMapper.selectList(wrapper);
        return list;
    }


}
