package com.yiling.marketing.lotteryactivity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinRuleDTO;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityJoinRuleDO;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityJoinRuleMapper;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityJoinRuleService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 抽奖活动-C端参与规则 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-13
 */
@Service
public class LotteryActivityJoinRuleServiceImpl extends BaseServiceImpl<LotteryActivityJoinRuleMapper, LotteryActivityJoinRuleDO> implements LotteryActivityJoinRuleService {

    @Override
    public LotteryActivityJoinRuleDTO getByLotteryActivityId(Long lotteryActivityId) {
        LambdaQueryWrapper<LotteryActivityJoinRuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityJoinRuleDO::getLotteryActivityId, lotteryActivityId);
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), LotteryActivityJoinRuleDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByLotteryActivityId(Long lotteryActivityId, Long opUserId) {
        LambdaQueryWrapper<LotteryActivityJoinRuleDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LotteryActivityJoinRuleDO::getLotteryActivityId, lotteryActivityId);

        LotteryActivityJoinRuleDO joinRuleDO = new LotteryActivityJoinRuleDO();
        joinRuleDO.setOpUserId(opUserId);
        this.batchDeleteWithFill(joinRuleDO, wrapper);
    }
}
