package com.yiling.user.integral.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.user.integral.dao.IntegralBehaviorPlatformMapper;
import com.yiling.user.integral.entity.IntegralBehaviorPlatformDO;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.user.integral.service.IntegralBehaviorPlatformService;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 积分行为适用平台表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-03
 */
@Service
public class IntegralBehaviorPlatformServiceImpl extends BaseServiceImpl<IntegralBehaviorPlatformMapper, IntegralBehaviorPlatformDO> implements IntegralBehaviorPlatformService {

    @Override
    public List<Integer> getByBehaviorId(Long behaviorId) {
        LambdaQueryWrapper<IntegralBehaviorPlatformDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralBehaviorPlatformDO::getBehaviorId, behaviorId);
        return this.list(wrapper).stream().map(IntegralBehaviorPlatformDO::getPlatform).distinct().collect(Collectors.toList());
    }
}
