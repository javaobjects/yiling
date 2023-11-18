package com.yiling.marketing.strategy.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.strategy.dao.StrategyAmountLadderMapper;
import com.yiling.marketing.strategy.dto.request.SaveStrategyAmountLadderRequest;
import com.yiling.marketing.strategy.dto.request.SaveStrategyGiftRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyAmountLadderDO;
import com.yiling.marketing.strategy.entity.StrategyGiftDO;
import com.yiling.marketing.strategy.service.StrategyAmountLadderService;
import com.yiling.marketing.strategy.service.StrategyGiftService;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 营销活动订单累计金额满赠内容阶梯 服务实现类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StrategyAmountLadderServiceImpl extends BaseServiceImpl<StrategyAmountLadderMapper, StrategyAmountLadderDO> implements StrategyAmountLadderService {

    private final StrategyGiftService giftService;

    @Override
    public boolean save(Long marketingStrategyId, List<SaveStrategyAmountLadderRequest> requestList, Long opUserId, Date opTime) {
        // 1.新增和修改的阶梯
        List<Long> idList = new ArrayList<>();
        for (SaveStrategyAmountLadderRequest amountLadderRequest : requestList) {
            if (Objects.isNull(amountLadderRequest)) {
                continue;
            }
            StrategyAmountLadderDO amountLadderDO = PojoUtils.map(amountLadderRequest, StrategyAmountLadderDO.class);
            amountLadderDO.setOpUserId(opUserId);
            amountLadderDO.setOpTime(opTime);
            if (Objects.nonNull(amountLadderRequest.getId())) {
                this.updateById(amountLadderDO);
            } else {
                amountLadderDO.setMarketingStrategyId(marketingStrategyId);
                this.save(amountLadderDO);
            }
            List<SaveStrategyGiftRequest> strategyGiftList = amountLadderRequest.getStrategyGiftList();
            giftService.save(marketingStrategyId, amountLadderDO.getId(), strategyGiftList, opUserId, opTime);
            idList.add(amountLadderDO.getId());
        }

        // 2.删除删掉的阶梯
        List<StrategyAmountLadderDO> amountLadderDOList = this.listAmountLadderByActivityId(marketingStrategyId);
        List<StrategyAmountLadderDO> deleteDOList = amountLadderDOList.stream().filter(e -> !idList.contains(e.getId())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(deleteDOList)) {
            List<Long> deleteIdList = deleteDOList.stream().map(StrategyAmountLadderDO::getId).collect(Collectors.toList());
            StrategyAmountLadderDO amountLadderDO = new StrategyAmountLadderDO();
            amountLadderDO.setDelFlag(1);
            amountLadderDO.setOpUserId(opUserId);
            amountLadderDO.setOpTime(opTime);
            QueryWrapper<StrategyAmountLadderDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(StrategyAmountLadderDO::getId, deleteIdList);
            this.batchDeleteWithFill(amountLadderDO, wrapper);
        }

        // 删除之前阶梯里面的赠品信息
        StrategyGiftDO giftDO = new StrategyGiftDO();
        giftDO.setDelFlag(1);
        giftDO.setOpUserId(opUserId);
        giftDO.setOpTime(opTime);
        QueryWrapper<StrategyGiftDO> giftWrapper = new QueryWrapper<>();
        giftWrapper.lambda().eq(StrategyGiftDO::getMarketingStrategyId, marketingStrategyId);
        giftWrapper.lambda().notIn(StrategyGiftDO::getLadderId, idList);
        giftService.batchDeleteWithFill(giftDO, giftWrapper);
        return true;
    }

    public static void main(String[] args) {
        List<Integer> oldList = new ArrayList<Integer>();
        //        {{
        //            add(1);
        //            add(2);
        //            add(3);
        //        }};
        List<Integer> newList = new ArrayList<Integer>() {{
            add(1);
            add(4);
        }};
        List<Integer> collect = oldList.stream().filter(e -> !newList.contains(e)).collect(Collectors.toList());
        System.out.println(collect);
    }

    @Override
    public void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime) {
        List<StrategyAmountLadderDO> strategyAmountLadderDOList = this.listAmountLadderByActivityId(oldId);
        for (StrategyAmountLadderDO strategyAmountLadderDO : strategyAmountLadderDOList) {
            Long oldLadderId = strategyAmountLadderDO.getId();
            strategyAmountLadderDO.setId(null);
            strategyAmountLadderDO.setMarketingStrategyId(strategyActivityDO.getId());
            strategyAmountLadderDO.setOpUserId(opUserId);
            strategyAmountLadderDO.setOpTime(opTime);
            this.save(strategyAmountLadderDO);

            giftService.copy(strategyActivityDO, oldId, strategyAmountLadderDO.getId(), oldLadderId, opUserId, opTime);
        }
    }

    @Override
    public List<StrategyAmountLadderDO> listAmountLadderByActivityId(Long strategyActivityId) {
        QueryWrapper<StrategyAmountLadderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyAmountLadderDO::getMarketingStrategyId, strategyActivityId);
        return this.list(wrapper);
    }

    @Override
    public List<StrategyAmountLadderDO> listAmountLadderByActivityIdAndAmountLimit(Long strategyActivityId, BigDecimal amountLimit) {
        QueryWrapper<StrategyAmountLadderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyAmountLadderDO::getMarketingStrategyId, strategyActivityId);
        if (Objects.nonNull(amountLimit)) {
            wrapper.lambda().eq(StrategyAmountLadderDO::getAmountLimit, amountLimit);
        }
        return this.list(wrapper);
    }
}
