package com.yiling.marketing.integral.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.integral.dao.IntegralBehaviorMapper;
import com.yiling.marketing.integral.entity.IntegralBehaviorDO;
import com.yiling.marketing.integral.service.IntegralBehaviorService;
import com.yiling.user.integral.dto.IntegralBehaviorDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 积分行为表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-12-29
 */
@Slf4j
@Service
public class IntegralBehaviorServiceImpl extends BaseServiceImpl<IntegralBehaviorMapper, IntegralBehaviorDO> implements IntegralBehaviorService {

    @Override
    public IntegralBehaviorDTO getByName(String name) {
        LambdaQueryWrapper<IntegralBehaviorDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralBehaviorDO::getName, name);
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), IntegralBehaviorDTO.class);
    }
}
