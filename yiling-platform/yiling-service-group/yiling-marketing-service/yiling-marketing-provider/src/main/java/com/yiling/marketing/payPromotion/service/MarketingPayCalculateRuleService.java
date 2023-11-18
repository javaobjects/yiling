package com.yiling.marketing.payPromotion.service;

import java.util.List;

import com.yiling.marketing.payPromotion.entity.MarketingPayCalculateRuleDO;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.strategy.dto.StrategyAmountLadderDTO;

/**
 * <p>
 * 支付促销计算规则表 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
public interface MarketingPayCalculateRuleService extends BaseService<MarketingPayCalculateRuleDO> {

    /**
     * 根据活动id查询促销规则
     *
     * @param activityId 活动id
     * @return 营销活动订单累计金额满赠内容阶梯
     */
    List<MarketingPayCalculateRuleDO> listRuleByActivityId(Long activityId);

}
