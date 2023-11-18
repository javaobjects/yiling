package com.yiling.marketing.strategy.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromoterMemberLimitDO;
import com.yiling.marketing.paypromotion.dto.PayPromotionPromoterMemberLimitDTO;
import com.yiling.marketing.presale.entity.MarketingPresalePromoterMemberLimitDO;
import com.yiling.marketing.strategy.api.StrategyPromoterMemberApi;
import com.yiling.marketing.strategy.dto.PresalePromoterMemberLimitDTO;
import com.yiling.marketing.strategy.dto.StrategyPromoterMemberLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddPresalePromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.AddStrategyPromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeletePresalePromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyPromoterMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryPresalePromoterMemberLimitPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyPromoterMemberLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategyPromoterMemberLimitDO;
import com.yiling.marketing.strategy.service.StrategyPromoterMemberLimitService;

import lombok.RequiredArgsConstructor;

/**
 * 策略满赠推广方会员Api
 *
 * @author: yong.zhang
 * @date: 2022/8/29
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StrategyPromoterMemberApiImpl implements StrategyPromoterMemberApi {

    private final StrategyPromoterMemberLimitService promoterMemberLimitService;

    @Override
    public boolean add(AddStrategyPromoterMemberLimitRequest request) {
        return promoterMemberLimitService.add(request);
    }

    @Override
    public boolean addForPayPromotion(AddStrategyPromoterMemberLimitRequest request) {
        return promoterMemberLimitService.addForPayPromotion(request);
    }

    @Override
    public boolean addForPresale(AddPresalePromoterMemberLimitRequest request) {
        return promoterMemberLimitService.addForPresale(request);
    }

    @Override
    public boolean delete(DeleteStrategyPromoterMemberLimitRequest request) {
        return promoterMemberLimitService.delete(request);
    }

    @Override
    public boolean deleteForPayPromotion(DeleteStrategyPromoterMemberLimitRequest request) {
        return promoterMemberLimitService.deleteForPayPromotion(request);
    }

    @Override
    public boolean deleteForPresale(DeletePresalePromoterMemberLimitRequest request) {
        return promoterMemberLimitService.deleteForPresale(request);
    }

    @Override
    public Page<StrategyPromoterMemberLimitDTO> pageList(QueryStrategyPromoterMemberLimitPageRequest request) {
        Page<StrategyPromoterMemberLimitDO> doPage = promoterMemberLimitService.pageList(request);
        return PojoUtils.map(doPage, StrategyPromoterMemberLimitDTO.class);
    }

    @Override
    public Page<PresalePromoterMemberLimitDTO> pageListForPresale(QueryPresalePromoterMemberLimitPageRequest request) {
        Page<MarketingPresalePromoterMemberLimitDO> doPage = promoterMemberLimitService.pageListForPresale(request);
        return PojoUtils.map(doPage, PresalePromoterMemberLimitDTO.class);
    }

    @Override
    public Integer countPromoterMemberByActivityId(Long strategyActivityId) {
        return promoterMemberLimitService.countPromoterMemberByActivityId(strategyActivityId);
    }

    @Override
    public List<StrategyPromoterMemberLimitDTO> listByActivityIdAndEidList(Long strategyActivityId, List<Long> eidList) {
        List<StrategyPromoterMemberLimitDO> doList = promoterMemberLimitService.listByActivityIdAndEidList(strategyActivityId, eidList);
        return PojoUtils.map(doList, StrategyPromoterMemberLimitDTO.class);
    }

    @Override
    public Page<PayPromotionPromoterMemberLimitDTO> pageListForPayPromotion(QueryStrategyPromoterMemberLimitPageRequest request) {
        Page<MarketingPayPromoterMemberLimitDO> doPage = promoterMemberLimitService.pageListForPayPromotion(request);
        return PojoUtils.map(doPage, PayPromotionPromoterMemberLimitDTO.class);
    }
}
