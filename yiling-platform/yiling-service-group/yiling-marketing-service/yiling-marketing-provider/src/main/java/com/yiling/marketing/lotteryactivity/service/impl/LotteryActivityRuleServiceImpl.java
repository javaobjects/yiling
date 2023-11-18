package com.yiling.marketing.lotteryactivity.service.impl;

import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityRuleDTO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityRulePageRequest;
import com.yiling.marketing.lotteryactivity.entity.LotteryActivityRuleDO;
import com.yiling.marketing.lotteryactivity.dao.LotteryActivityRuleMapper;
import com.yiling.marketing.lotteryactivity.service.LotteryActivityRuleService;
import com.yiling.framework.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 抽奖活动规则表（内置数据表） 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2022-09-13
 */
@Service
public class LotteryActivityRuleServiceImpl extends BaseServiceImpl<LotteryActivityRuleMapper, LotteryActivityRuleDO> implements LotteryActivityRuleService {

    @Override
    public Page<LotteryActivityRuleDTO> queryRulePage(QueryLotteryActivityRulePageRequest request) {
        LambdaQueryWrapper<LotteryActivityRuleDO> wrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(request.getUsePlatform()) && request.getUsePlatform() != 0) {
            wrapper.eq(LotteryActivityRuleDO::getUsePlatform, request.getUsePlatform());
        }
        return PojoUtils.map(this.page(request.getPage(), wrapper), LotteryActivityRuleDTO.class);
    }
}
