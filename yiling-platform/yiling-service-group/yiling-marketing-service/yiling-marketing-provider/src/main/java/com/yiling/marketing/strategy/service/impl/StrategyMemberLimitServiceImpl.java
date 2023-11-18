package com.yiling.marketing.strategy.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.payPromotion.entity.MarketingPayMemberLimitDO;
import com.yiling.marketing.payPromotion.service.MarketingPayMemberLimitService;
import com.yiling.marketing.presale.entity.MarketingPresaleGoodsLimitDO;
import com.yiling.marketing.presale.entity.MarketingPresaleMemberLimitDO;
import com.yiling.marketing.presale.service.MarketingPresaleGoodsLimitService;
import com.yiling.marketing.presale.service.MarketingPresaleMemberLimitService;
import com.yiling.marketing.strategy.dao.StrategyMemberLimitMapper;
import com.yiling.marketing.strategy.dto.request.AddPresaleMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.AddStrategyMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeletePresaleMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyMemberLimitRequest;
import com.yiling.marketing.strategy.dto.request.QueryPresaleMemberLimitPageRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyMemberLimitPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyMemberLimitDO;
import com.yiling.marketing.strategy.enums.StrategyErrorCode;
import com.yiling.marketing.strategy.service.StrategyMemberLimitService;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.dto.MemberDetailDTO;

/**
 * <p>
 * 策略满赠会员方案 服务实现类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Service
public class StrategyMemberLimitServiceImpl extends BaseServiceImpl<StrategyMemberLimitMapper, StrategyMemberLimitDO> implements StrategyMemberLimitService {

    @DubboReference
    MemberApi memberApi;

    @Autowired
    MarketingPayMemberLimitService marketingPayMemberLimitService;

    @Autowired
    MarketingPresaleMemberLimitService memberLimitService;

    @Autowired
    MarketingPresaleGoodsLimitService presaleGoodsLimitService;
    @Override
    public List<Long> listMemberIdByStrategyId(Long strategyActivityId) {
        return this.getBaseMapper().listMemberIdByStrategyId(strategyActivityId);
    }

    @Override
    public boolean add(AddStrategyMemberLimitRequest request) {
        StrategyMemberLimitDO strategyMemberLimitDO = this.queryByActivityIdAndMemberId(request.getMarketingStrategyId(), request.getMemberId());
        if (Objects.nonNull(strategyMemberLimitDO)) {
            throw new BusinessException(StrategyErrorCode.STRATEGY_DATA_IS_EXISTS);
        }
        StrategyMemberLimitDO memberLimitDO = PojoUtils.map(request, StrategyMemberLimitDO.class);
        // 添加会员名称
        MemberDetailDTO memberDetailDTO = memberApi.getMember(request.getMemberId());
        memberLimitDO.setMemberName(memberDetailDTO.getName());
        return this.save(memberLimitDO);
    }

    @Override
    public boolean addForPromotion(AddStrategyMemberLimitRequest request) {
        QueryWrapper<MarketingPayMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayMemberLimitDO::getMarketingPayId, request.getMarketingStrategyId());
        wrapper.lambda().eq(MarketingPayMemberLimitDO::getMemberId, request.getMemberId()
        );
        wrapper.lambda().last(" limit 1");
        MarketingPayMemberLimitDO strategyMemberLimitDO = marketingPayMemberLimitService.getOne(wrapper);
        if (Objects.nonNull(strategyMemberLimitDO)) {
            return false;
        }
        MarketingPayMemberLimitDO memberLimitDO = PojoUtils.map(request, MarketingPayMemberLimitDO.class);
        memberLimitDO.setMarketingPayId(request.getMarketingStrategyId());
        // 添加会员名称
        MemberDetailDTO memberDetailDTO = memberApi.getMember(request.getMemberId());
        memberLimitDO.setMemberName(memberDetailDTO.getName());
        return marketingPayMemberLimitService.save(memberLimitDO);
    }

    @Override
    public boolean addForPresale(AddPresaleMemberLimitRequest request) {
        MarketingPresaleMemberLimitDO strategyMemberLimitDO = this.queryByActivityIdAndMemberIdForPresale(request.getMarketingPresaleId(), request.getMemberId());
        if (Objects.nonNull(strategyMemberLimitDO)) {
            throw new BusinessException(StrategyErrorCode.STRATEGY_DATA_IS_EXISTS);
        }
        MarketingPresaleMemberLimitDO memberLimitDO = PojoUtils.map(request, MarketingPresaleMemberLimitDO.class);
        // 添加会员名称
        MemberDetailDTO memberDetailDTO = memberApi.getMember(request.getMemberId());
        memberLimitDO.setMemberName(memberDetailDTO.getName());
        return memberLimitService.save(memberLimitDO);
    }

    @Override
    public void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime) {
        List<StrategyMemberLimitDO> strategyMemberLimitDOList = this.listMemberByActivityId(oldId);
        for (StrategyMemberLimitDO memberLimitDO : strategyMemberLimitDOList) {
            memberLimitDO.setId(null);
            memberLimitDO.setMarketingStrategyId(strategyActivityDO.getId());
            memberLimitDO.setOpUserId(opUserId);
            memberLimitDO.setOpTime(opTime);
        }
        this.saveBatch(strategyMemberLimitDOList);
    }

    @Override
    public boolean delete(DeleteStrategyMemberLimitRequest request) {
        StrategyMemberLimitDO memberLimitDO = new StrategyMemberLimitDO();
        memberLimitDO.setDelFlag(1);
        memberLimitDO.setOpUserId(request.getOpUserId());
        memberLimitDO.setOpTime(request.getOpTime());
        QueryWrapper<StrategyMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyMemberLimitDO::getMarketingStrategyId, request.getMarketingStrategyId());
        wrapper.lambda().eq(StrategyMemberLimitDO::getMemberId, request.getMemberId());
        this.batchDeleteWithFill(memberLimitDO, wrapper);
        return true;
    }

    @Override
    public boolean deleteForPresale(DeletePresaleMemberLimitRequest request) {
        MarketingPresaleMemberLimitDO memberLimitDO = new MarketingPresaleMemberLimitDO();
        memberLimitDO.setDelFlag(1);
        memberLimitDO.setOpUserId(request.getOpUserId());
        memberLimitDO.setOpTime(request.getOpTime());
        QueryWrapper<MarketingPresaleMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPresaleMemberLimitDO::getId, request.getId());
        memberLimitService.batchDeleteWithFill(memberLimitDO, wrapper);
        return true;
    }
    @Override
    public boolean deleteForPresaleGoodsLimit(DeletePresaleMemberLimitRequest request) {
        MarketingPresaleGoodsLimitDO memberLimitDO = new MarketingPresaleGoodsLimitDO();
        memberLimitDO.setDelFlag(1);
        memberLimitDO.setOpUserId(request.getOpUserId());
        memberLimitDO.setOpTime(request.getOpTime());
        QueryWrapper<MarketingPresaleGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPresaleGoodsLimitDO::getId, request.getId());
        presaleGoodsLimitService.batchDeleteWithFill(memberLimitDO, wrapper);
        return true;
    }

    @Override
    public boolean deleteForPromotion(DeleteStrategyMemberLimitRequest request) {
        MarketingPayMemberLimitDO memberLimitDO = new MarketingPayMemberLimitDO();
        memberLimitDO.setDelFlag(1);
        memberLimitDO.setOpUserId(request.getOpUserId());
        memberLimitDO.setOpTime(request.getOpTime());
        QueryWrapper<MarketingPayMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayMemberLimitDO::getMarketingPayId, request.getMarketingStrategyId());
        wrapper.lambda().eq(MarketingPayMemberLimitDO::getMemberId, request.getMemberId());
        marketingPayMemberLimitService.batchDeleteWithFill(memberLimitDO, wrapper);
        return true;
    }

    @Override
    public Page<StrategyMemberLimitDO> pageList(QueryStrategyMemberLimitPageRequest request) {
        QueryWrapper<StrategyMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyMemberLimitDO::getMarketingStrategyId, request.getMarketingStrategyId());
        wrapper.lambda().orderByDesc(StrategyMemberLimitDO::getCreateTime);
        return this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
    }

    @Override
    public Page<MarketingPayMemberLimitDO> pageListForPayPromotion(QueryStrategyMemberLimitPageRequest request) {
        QueryWrapper<MarketingPayMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayMemberLimitDO::getMarketingPayId, request.getMarketingStrategyId());
        wrapper.lambda().orderByDesc(MarketingPayMemberLimitDO::getCreateTime);
        return marketingPayMemberLimitService.page(request.getPage(), wrapper);
    }

    @Override
    public Page<MarketingPresaleMemberLimitDO> pageListForPresale(QueryPresaleMemberLimitPageRequest request) {
        QueryWrapper<MarketingPresaleMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPresaleMemberLimitDO::getMarketingPresaleId, request.getMarketingPresaleId());
        wrapper.lambda().orderByDesc(MarketingPresaleMemberLimitDO::getCreateTime);
        return memberLimitService.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
    }

    @Override
    public Integer countMemberByActivityId(Long strategyActivityId) {
        QueryWrapper<StrategyMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyMemberLimitDO::getMarketingStrategyId, strategyActivityId);
        return this.count(wrapper);
    }

    @Override
    public List<StrategyMemberLimitDO> listMemberByActivityId(Long strategyActivityId) {
        QueryWrapper<StrategyMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyMemberLimitDO::getMarketingStrategyId, strategyActivityId);
        return this.list(wrapper);
    }

    @Override
    public StrategyMemberLimitDO queryByActivityIdAndMemberId(Long strategyActivityId, Long memberId) {
        QueryWrapper<StrategyMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyMemberLimitDO::getMarketingStrategyId, strategyActivityId);
        wrapper.lambda().eq(StrategyMemberLimitDO::getMemberId, memberId);
        wrapper.lambda().last(" limit 1");
        return this.getOne(wrapper);
    }

    public MarketingPresaleMemberLimitDO queryByActivityIdAndMemberIdForPresale(Long strategyActivityId, Long memberId) {
        QueryWrapper<MarketingPresaleMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPresaleMemberLimitDO::getMarketingPresaleId, strategyActivityId);
        wrapper.lambda().eq(MarketingPresaleMemberLimitDO::getMemberId, memberId);
        wrapper.lambda().last(" limit 1");
        return memberLimitService.getOne(wrapper);
    }

    @Override
    public List<StrategyMemberLimitDO> listByActivityIdAndMemberIdList(Long strategyActivityId, List<Long> memberIdList) {
        QueryWrapper<StrategyMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyMemberLimitDO::getMarketingStrategyId, strategyActivityId);
        wrapper.lambda().in(StrategyMemberLimitDO::getMemberId, memberIdList);
        return this.list(wrapper);
    }
}
