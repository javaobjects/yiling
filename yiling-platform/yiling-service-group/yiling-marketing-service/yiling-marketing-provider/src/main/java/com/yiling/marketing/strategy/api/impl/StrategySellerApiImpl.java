package com.yiling.marketing.strategy.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.strategy.api.StrategySellerApi;
import com.yiling.marketing.strategy.dto.StrategySellerLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddStrategySellerLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategySellerLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategySellerLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategySellerLimitDO;
import com.yiling.marketing.strategy.service.StrategySellerLimitService;

import lombok.RequiredArgsConstructor;

/**
 * 策略满赠商家Api
 *
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StrategySellerApiImpl implements StrategySellerApi {

    private final StrategySellerLimitService sellerLimitService;

    @Override
    public boolean add(AddStrategySellerLimitRequest request) {
        return sellerLimitService.add(request);
    }

    @Override
    public boolean delete(DeleteStrategySellerLimitRequest request) {
        return sellerLimitService.delete(request);
    }

    @Override
    public Page<StrategySellerLimitDTO> pageList(QueryStrategySellerLimitPageRequest request) {
        Page<StrategySellerLimitDO> doPage = sellerLimitService.pageList(request);
        return PojoUtils.map(doPage, StrategySellerLimitDTO.class);
    }

    @Override
    public Integer countSellerByActivityId(Long strategyActivityId) {
        return sellerLimitService.countSellerByActivityId(strategyActivityId);
    }

    @Override
    public List<StrategySellerLimitDTO> listSellerByActivityId(Long strategyActivityId) {
        List<StrategySellerLimitDO> doList = sellerLimitService.listSellerByActivityId(strategyActivityId);
        return PojoUtils.map(doList, StrategySellerLimitDTO.class);
    }

    @Override
    public List<StrategySellerLimitDTO> listByActivityIdAndEidList(Long strategyActivityId, List<Long> eidList) {
        List<StrategySellerLimitDO> doList = sellerLimitService.listByActivityIdAndEidList(strategyActivityId, eidList);
        return PojoUtils.map(doList, StrategySellerLimitDTO.class);
    }

    @Override
    public Integer countSellerByActivityIdForPayPromotion(Long strategyActivityId) {
        return sellerLimitService.countSellerByActivityIdForPayPromotion(strategyActivityId);
    }
}
