package com.yiling.marketing.strategy.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.strategy.api.StrategyGiftApi;
import com.yiling.marketing.strategy.dto.StrategyGiftDTO;
import com.yiling.marketing.strategy.entity.StrategyGiftDO;
import com.yiling.marketing.strategy.service.StrategyGiftService;

import lombok.RequiredArgsConstructor;

/**
 * @author: yong.zhang
 * @date: 2022/8/23
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StrategyGiftApiImpl implements StrategyGiftApi {

    private final StrategyGiftService giftService;

    @Override
    public List<StrategyGiftDTO> listGiftByActivityIdAndLadderId(Long strategyActivityId, Long ladderId) {
        List<StrategyGiftDO> doList = giftService.listGiftByActivityIdAndLadderId(strategyActivityId, ladderId);
        return PojoUtils.map(doList, StrategyGiftDTO.class);
    }
}
