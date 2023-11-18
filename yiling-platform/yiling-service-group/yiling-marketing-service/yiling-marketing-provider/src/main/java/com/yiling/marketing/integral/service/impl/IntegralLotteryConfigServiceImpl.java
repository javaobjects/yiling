package com.yiling.marketing.integral.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.integral.dao.IntegralLotteryConfigMapper;
import com.yiling.marketing.integral.entity.IntegralLotteryConfigDO;
import com.yiling.marketing.integral.service.IntegralLotteryConfigService;
import com.yiling.user.integral.dto.IntegralLotteryConfigDTO;

/**
 * <p>
 * 积分参与抽奖活动配置表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-01-06
 */
@Service
public class IntegralLotteryConfigServiceImpl extends BaseServiceImpl<IntegralLotteryConfigMapper, IntegralLotteryConfigDO> implements IntegralLotteryConfigService {

    @Override
    public IntegralLotteryConfigDTO getByUseRuleId(Long useRuleId) {
        LambdaQueryWrapper<IntegralLotteryConfigDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IntegralLotteryConfigDO::getUseRuleId, useRuleId);
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), IntegralLotteryConfigDTO.class);
    }
}
