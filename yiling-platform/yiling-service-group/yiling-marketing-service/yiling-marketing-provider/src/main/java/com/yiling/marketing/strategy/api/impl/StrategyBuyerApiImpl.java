package com.yiling.marketing.strategy.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.payPromotion.entity.MarketingPayBuyerLimitDO;
import com.yiling.marketing.paypromotion.dto.PayPromotionBuyerLimitDTO;
import com.yiling.marketing.presale.entity.MarketingPresaleBuyerLimitDO;
import com.yiling.marketing.strategy.api.StrategyBuyerApi;
import com.yiling.marketing.strategy.dto.PresaleBuyerLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyBuyerLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddPresaleBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.AddStrategyBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeletePresaleBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyBuyerLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryPresaleBuyerLimitPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyBuyerLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategyBuyerLimitDO;
import com.yiling.marketing.strategy.service.StrategyBuyerLimitService;

import lombok.RequiredArgsConstructor;

/**
 * 策略满赠客户Api
 *
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StrategyBuyerApiImpl implements StrategyBuyerApi {

    private final StrategyBuyerLimitService buyerLimitService;

    @Override
    public boolean add(AddStrategyBuyerLimitRequest request) {
        return buyerLimitService.add(request);
    }

    @Override
    public boolean addForPayPromotion(AddStrategyBuyerLimitRequest request) {
        return buyerLimitService.addForPayPromotion(request);
    }
    @Override
    public boolean addForPresale(AddPresaleBuyerLimitRequest request) {
        return buyerLimitService.addForPresale(request);
    }

    @Override
    public boolean delete(DeleteStrategyBuyerLimitRequest request) {
        return buyerLimitService.delete(request);
    }

    @Override
    public boolean deleteForPayPromotion(DeleteStrategyBuyerLimitRequest request) {
        return buyerLimitService.deleteForPayPromotion(request);
    }

    @Override
    public Page<StrategyBuyerLimitDTO> pageList(QueryStrategyBuyerLimitPageRequest request) {
        Page<StrategyBuyerLimitDO> doPage = buyerLimitService.pageList(request);
        return PojoUtils.map(doPage, StrategyBuyerLimitDTO.class);
    }
    @Override
    public Page<PayPromotionBuyerLimitDTO> pageListForPromotion(QueryStrategyBuyerLimitPageRequest request) {
        Page<MarketingPayBuyerLimitDO> doPage = buyerLimitService.pageListForPromotion(request);
        return PojoUtils.map(doPage, PayPromotionBuyerLimitDTO.class);
    }

    @Override
    public Page<PresaleBuyerLimitDTO> pageListForPresale(QueryPresaleBuyerLimitPageRequest request) {
        Page<MarketingPresaleBuyerLimitDO> doPage = buyerLimitService.pageListForPresale(request);
        return PojoUtils.map(doPage, PresaleBuyerLimitDTO.class);
    }

    @Override
    public Integer countBuyerByActivityId(Long strategyActivityId) {
        return buyerLimitService.countBuyerByActivityId(strategyActivityId);
    }

    @Override
    public List<StrategyBuyerLimitDTO> listBuyerByActivityId(Long strategyActivityId) {
        List<StrategyBuyerLimitDO> doList = buyerLimitService.listBuyerByActivityId(strategyActivityId);
        return PojoUtils.map(doList, StrategyBuyerLimitDTO.class);
    }

    @Override
    public List<StrategyBuyerLimitDTO> listByActivityIdAndEidList(Long strategyActivityId, List<Long> eidList) {
        List<StrategyBuyerLimitDO> doList = buyerLimitService.listByActivityIdAndEidList(strategyActivityId, eidList);
        return PojoUtils.map(doList, StrategyBuyerLimitDTO.class);
    }

    @Override
    public List<StrategyBuyerLimitDTO> listByActivityIdListAndEid(List<Long> marketingStrategyIdList, Long buyerEid) {
        List<StrategyBuyerLimitDO> doList = buyerLimitService.listByActivityIdListAndEid(marketingStrategyIdList, buyerEid);
        return PojoUtils.map(doList, StrategyBuyerLimitDTO.class);
    }

    @Override
    public boolean deleteForPresale(DeletePresaleBuyerLimitRequest request){
        return buyerLimitService.deleteForPresale(request);
    }

    @Override
    public Integer countBuyerByActivityIdForPayPromotion(Long strategyActivityId) {
        return buyerLimitService.countBuyerByActivityIdForPayPromotion(strategyActivityId);
    }

    @Override
    public Integer countMemberByActivityId(Long strategyActivityId) {
        return buyerLimitService.countMemberByActivityId(strategyActivityId);
    }

    @Override
    public Integer countPromoterMemberByActivityId(Long strategyActivityId) {
        return buyerLimitService.countPromoterMemberByActivityId(strategyActivityId);
    }
}
