package com.yiling.marketing.strategy.api;

import java.util.List;

import com.yiling.marketing.strategy.dto.StrategyGiftDTO;

/**
 * @author: yong.zhang
 * @date: 2022/8/23
 */
public interface StrategyGiftApi {
    
    /**
     * 查询营销活动赠品信息
     *
     * @param strategyActivityId 营销活动id
     * @param ladderId 活动阶梯id
     * @return 营销活动赠品信息
     */
    List<StrategyGiftDTO> listGiftByActivityIdAndLadderId(Long strategyActivityId, Long ladderId);
}
