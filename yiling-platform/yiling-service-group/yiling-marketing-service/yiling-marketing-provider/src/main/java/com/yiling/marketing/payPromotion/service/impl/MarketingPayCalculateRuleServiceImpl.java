package com.yiling.marketing.payPromotion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.payPromotion.dao.MarketingPayCalculateRuleMapper;
import com.yiling.marketing.payPromotion.entity.MarketingPayCalculateRuleDO;
import com.yiling.marketing.payPromotion.service.MarketingPayCalculateRuleService;

/**
 * <p>
 * 支付促销计算规则表 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
@Service
public class MarketingPayCalculateRuleServiceImpl extends BaseServiceImpl<MarketingPayCalculateRuleMapper, MarketingPayCalculateRuleDO> implements MarketingPayCalculateRuleService {

    @Override
    public List<MarketingPayCalculateRuleDO> listRuleByActivityId(Long activityId) {
        QueryWrapper<MarketingPayCalculateRuleDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayCalculateRuleDO::getMarketingPayId, activityId);
        return this.list(wrapper);
    }
}
