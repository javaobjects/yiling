package com.yiling.marketing.strategy.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.strategy.dao.StrategyGiftFailMapper;
import com.yiling.marketing.strategy.entity.StrategyGiftFailDO;
import com.yiling.marketing.strategy.service.StrategyGiftFailService;

/**
 * <p>
 * 策略满赠赠送赠品失败记录表 服务实现类
 * </p>
 *
 * @author zhangy
 * @date 2022-09-20
 */
@Service
public class StrategyGiftFailServiceImpl extends BaseServiceImpl<StrategyGiftFailMapper, StrategyGiftFailDO> implements StrategyGiftFailService {

    @Override
    public List<StrategyGiftFailDO> listByRecordId(Long activityRecordId) {
        QueryWrapper<StrategyGiftFailDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyGiftFailDO::getActivityRecordId, activityRecordId);
        return this.list(wrapper);
    }
}
