package com.yiling.marketing.strategy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.strategy.dao.StrategyStageMemberEffectMapper;
import com.yiling.marketing.strategy.dto.request.AddStrategyStageMemberEffectRequest;
import com.yiling.marketing.strategy.dto.request.DeleteStrategyStageMemberEffectRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyStageMemberEffectPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyStageMemberEffectDO;
import com.yiling.marketing.strategy.service.StrategyGiftService;
import com.yiling.marketing.strategy.service.StrategyStageMemberEffectService;
import com.yiling.user.member.api.MemberApi;
import com.yiling.user.member.api.MemberBuyStageApi;
import com.yiling.user.member.dto.MemberBuyStageDTO;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 策略满赠购买会员方案表 服务实现类
 * </p>
 *
 * @author zhangy
 * @date 2022-09-05
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StrategyStageMemberEffectServiceImpl extends BaseServiceImpl<StrategyStageMemberEffectMapper, StrategyStageMemberEffectDO> implements StrategyStageMemberEffectService {

    @DubboReference
    MemberApi memberApi;

    @DubboReference
    MemberBuyStageApi memberBuyStageApi;

    private final StrategyGiftService giftService;

    @Override
    public boolean add(AddStrategyStageMemberEffectRequest request) {
        if (Objects.nonNull(request.getMemberId())) {
            List<StrategyStageMemberEffectDO> effectDOList = this.listByActivityIdAndMemberIdList(request.getMarketingStrategyId(), new ArrayList<Long>() {{
                add(request.getMemberId());
            }});
            if (CollUtil.isNotEmpty(effectDOList)) {
                return false;
            }
            StrategyStageMemberEffectDO stageMemberEffectDO = PojoUtils.map(request, StrategyStageMemberEffectDO.class);
            MemberBuyStageDTO memberBuyStageDTO = memberBuyStageApi.getById(request.getMemberId());
            stageMemberEffectDO.setMemberName(memberBuyStageDTO.getMemberName());
            return this.save(stageMemberEffectDO);
        } else if (CollUtil.isNotEmpty(request.getMemberIdList())) {
            List<MemberBuyStageDTO> memberBuyStageDTOList = memberBuyStageApi.listByIds(request.getMemberIdList());
            Map<Long, MemberBuyStageDTO> memberBuyStageMap = memberBuyStageDTOList.stream().collect(Collectors.toMap(MemberBuyStageDTO::getId, e -> e, (k1, k2) -> k1));
            List<StrategyStageMemberEffectDO> stageMemberEffectDOList = new ArrayList<>();
            for (Long memberId : request.getMemberIdList()) {
                List<StrategyStageMemberEffectDO> effectDOList = this.listByActivityIdAndMemberIdList(request.getMarketingStrategyId(), new ArrayList<Long>() {{
                    add(memberId);
                }});
                if (CollUtil.isNotEmpty(effectDOList)) {
                    continue;
                }
                StrategyStageMemberEffectDO stageMemberEffectDO = new StrategyStageMemberEffectDO();
                stageMemberEffectDO.setMarketingStrategyId(request.getMarketingStrategyId());
                stageMemberEffectDO.setMemberId(memberId);
                MemberBuyStageDTO memberBuyStageDTO = memberBuyStageMap.get(memberId);
                if (Objects.nonNull(memberBuyStageDTO)) {
                    stageMemberEffectDO.setMemberName(memberBuyStageDTO.getMemberName());
                }
                stageMemberEffectDO.setOpUserId(request.getOpUserId());
                stageMemberEffectDO.setOpTime(request.getOpTime());
                stageMemberEffectDOList.add(stageMemberEffectDO);
            }
            if (CollUtil.isNotEmpty(stageMemberEffectDOList)) {
                return this.saveBatch(stageMemberEffectDOList);
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime) {
        List<StrategyStageMemberEffectDO> stageMemberEffectDOList = this.listByActivityId(oldId);
        for (StrategyStageMemberEffectDO stageMemberEffectDO : stageMemberEffectDOList) {
            stageMemberEffectDO.setId(null);
            stageMemberEffectDO.setMarketingStrategyId(strategyActivityDO.getId());
            stageMemberEffectDO.setOpUserId(opUserId);
            stageMemberEffectDO.setOpTime(opTime);
        }
        this.saveBatch(stageMemberEffectDOList);

        giftService.copy(strategyActivityDO, oldId, null, null, opUserId, opTime);
    }

    @Override
    public boolean delete(DeleteStrategyStageMemberEffectRequest request) {
        StrategyStageMemberEffectDO stageMemberEffectDO = new StrategyStageMemberEffectDO();
        stageMemberEffectDO.setDelFlag(1);
        stageMemberEffectDO.setOpUserId(request.getOpUserId());
        stageMemberEffectDO.setOpTime(request.getOpTime());
        QueryWrapper<StrategyStageMemberEffectDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyStageMemberEffectDO::getMarketingStrategyId, request.getMarketingStrategyId());
        if (Objects.nonNull(request.getMemberId())) {
            wrapper.lambda().eq(StrategyStageMemberEffectDO::getMemberId, request.getMemberId());
        } else if (CollUtil.isNotEmpty(request.getMemberIdList())) {
            wrapper.lambda().in(StrategyStageMemberEffectDO::getMemberId, request.getMemberIdList());
        } else {
            return false;
        }
        this.batchDeleteWithFill(stageMemberEffectDO, wrapper);
        return true;
    }

    @Override
    public Page<StrategyStageMemberEffectDO> pageList(QueryStrategyStageMemberEffectPageRequest request) {
        QueryWrapper<StrategyStageMemberEffectDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyStageMemberEffectDO::getMarketingStrategyId, request.getMarketingStrategyId());
        if (StringUtils.isNotBlank(request.getMemberName())) {
            wrapper.lambda().like(StrategyStageMemberEffectDO::getMemberName, request.getMemberName());
        }
        Page<StrategyStageMemberEffectDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return this.page(objectPage, wrapper);
    }

    @Override
    public Integer countStageMemberEffectByActivityId(Long strategyActivityId) {
        QueryWrapper<StrategyStageMemberEffectDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyStageMemberEffectDO::getMarketingStrategyId, strategyActivityId);
        return this.count(wrapper);
    }

    @Override
    public List<StrategyStageMemberEffectDO> listByActivityId(Long strategyActivityId) {
        QueryWrapper<StrategyStageMemberEffectDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyStageMemberEffectDO::getMarketingStrategyId, strategyActivityId);
        return this.list(wrapper);
    }

    @Override
    public List<StrategyStageMemberEffectDO> listByActivityIdAndMemberIdList(Long strategyActivityId, List<Long> memberIdList) {
        QueryWrapper<StrategyStageMemberEffectDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyStageMemberEffectDO::getMarketingStrategyId, strategyActivityId);
        if (CollUtil.isNotEmpty(memberIdList)) {
            wrapper.lambda().in(StrategyStageMemberEffectDO::getMemberId, memberIdList);
        }
        return this.list(wrapper);
    }
}
