package com.yiling.marketing.lotteryactivity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityRuleInstructionMapper;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRuleInstructionDTO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityRuleInstructionDO;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityRuleInstructionService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 活动规则说明表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-08-29
 */
@Service
public class LotteryActivityRuleInstructionServiceImpl extends BaseServiceImpl<LotteryActivityRuleInstructionMapper, LotteryActivityRuleInstructionDO> implements LotteryActivityRuleInstructionService {

    @Override
    public LotteryActivityRuleInstructionDTO getByLotteryActivityId(Long lotteryActivityId) {
        LambdaQueryWrapper<LotteryActivityRuleInstructionDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityRuleInstructionDO::getLotteryActivityId, lotteryActivityId);
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), LotteryActivityRuleInstructionDTO.class);
    }
}
