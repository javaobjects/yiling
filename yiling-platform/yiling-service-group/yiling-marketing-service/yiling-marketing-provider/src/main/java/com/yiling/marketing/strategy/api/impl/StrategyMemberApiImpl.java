package com.yiling.marketing.strategy.api.impl;

import java.util.Date;
import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.payPromotion.entity.MarketingPayMemberLimitDO;
import com.yiling.marketing.paypromotion.dto.PaypromotionMemberLimitDTO;
import com.yiling.marketing.presale.entity.MarketingPresaleActivityDO;
import com.yiling.marketing.presale.entity.MarketingPresaleMemberLimitDO;
import com.yiling.marketing.presale.service.MarketingPresaleActivityService;
import com.yiling.marketing.strategy.api.StrategyMemberApi;
import com.yiling.marketing.strategy.dto.StrategyMemberLimitDTO;
import com.yiling.marketing.strategy.dto.request.AddPresaleMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.AddStrategyMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeletePresaleMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryPresaleMemberLimitPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyMemberLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategyMemberLimitDO;
import com.yiling.marketing.strategy.service.StrategyMemberLimitService;

import lombok.RequiredArgsConstructor;

/**
 * 策略满赠-指定方案会员Api
 *
 * @author: yong.zhang
 * @date: 2022/8/24
 */
@DubboService
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StrategyMemberApiImpl implements StrategyMemberApi {

    private final StrategyMemberLimitService memberLimitService;

    private final MarketingPresaleActivityService presaleActivityService;

    @Override
    public List<Long> listMemberIdByStrategyId(Long strategyActivityId) {
        return memberLimitService.listMemberIdByStrategyId(strategyActivityId);
    }

    @Override
    public boolean add(AddStrategyMemberLimitRequest request) {
        return memberLimitService.add(request);
    }

    @Override
    public boolean addForPromotion(AddStrategyMemberLimitRequest request) {
        return memberLimitService.addForPromotion(request);
    }

    @Override
    public boolean addForPresale(AddPresaleMemberLimitRequest request) {
        return memberLimitService.addForPresale(request);
    }

    @Override
    public boolean delete(DeleteStrategyMemberLimitRequest request) {
        return memberLimitService.delete(request);
    }

    @Override
    public boolean deleteForPromotion(DeleteStrategyMemberLimitRequest request) {
        return memberLimitService.deleteForPromotion(request);
    }

    @Override
    public Page<StrategyMemberLimitDTO> pageList(QueryStrategyMemberLimitPageRequest request) {
        Page<StrategyMemberLimitDO> doPage = memberLimitService.pageList(request);
        return PojoUtils.map(doPage, StrategyMemberLimitDTO.class);
    }

    @Override
    public Page<StrategyMemberLimitDTO> pageListForPresale(QueryPresaleMemberLimitPageRequest request) {
        Page<MarketingPresaleMemberLimitDO> doPage = memberLimitService.pageListForPresale(request);
        return PojoUtils.map(doPage, StrategyMemberLimitDTO.class);
    }

    @Override
    public Page<PaypromotionMemberLimitDTO> pageListForPayPromotion(QueryStrategyMemberLimitPageRequest request) {
        Page<MarketingPayMemberLimitDO> doPage = memberLimitService.pageListForPayPromotion(request);
        return PojoUtils.map(doPage, PaypromotionMemberLimitDTO.class);
    }

    @Override
    public Integer countMemberByActivityId(Long strategyActivityId) {
        return memberLimitService.countMemberByActivityId(strategyActivityId);
    }

    @Override
    public List<StrategyMemberLimitDTO> listMemberByActivityId(Long strategyActivityId) {
        List<StrategyMemberLimitDO> doList = memberLimitService.listMemberByActivityId(strategyActivityId);
        return PojoUtils.map(doList, StrategyMemberLimitDTO.class);
    }

    @Override
    public StrategyMemberLimitDTO queryByActivityIdAndMemberId(Long strategyActivityId, Long memberId) {
        StrategyMemberLimitDO memberLimitDO = memberLimitService.queryByActivityIdAndMemberId(strategyActivityId, memberId);
        return PojoUtils.map(memberLimitDO, StrategyMemberLimitDTO.class);
    }

    @Override
    public boolean deleteForPresale(DeletePresaleMemberLimitRequest request) {
        return memberLimitService.deleteForPresale(request);
    }

    @Override
    public boolean deleteForPresaleGoodsLimit(DeletePresaleMemberLimitRequest request) {
        return memberLimitService.deleteForPresaleGoodsLimit(request);
    }

    @Override
    public boolean updatePresaleStatus(Long currentUserId, Long id) {
        MarketingPresaleActivityDO presaleActivityDO = new MarketingPresaleActivityDO();
        presaleActivityDO.setId(id);
        presaleActivityDO.setOpUserId(currentUserId);
        presaleActivityDO.setOpTime(new Date());
        presaleActivityDO.setStatus(2);
        presaleActivityService.updateById(presaleActivityDO);
        return true;
    }


    @Override
    public List<StrategyMemberLimitDTO> listByActivityIdAndMemberIdList(Long strategyActivityId, List<Long> memberIdList) {
        List<StrategyMemberLimitDO> doList = memberLimitService.listByActivityIdAndMemberIdList(strategyActivityId, memberIdList);
        return PojoUtils.map(doList, StrategyMemberLimitDTO.class);
    }
}
