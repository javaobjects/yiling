package com.yiling.marketing.strategy.service.impl;

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
import com.yiling.marketing.strategy.dao.StrategyCycleLadderMapper;
import com.yiling.marketing.strategy.dto.request.SaveStrategyCycleLadderRequest;
import com.yiling.marketing.strategy.dto.request.SaveStrategyGiftRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyCycleLadderDO;
import com.yiling.marketing.strategy.entity.StrategyGiftDO;
import com.yiling.marketing.strategy.service.StrategyCycleLadderService;
import com.yiling.marketing.strategy.service.StrategyGiftService;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 营销活动时间周期满赠内容阶梯 服务实现类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StrategyCycleLadderServiceImpl extends BaseServiceImpl<StrategyCycleLadderMapper, StrategyCycleLadderDO> implements StrategyCycleLadderService {

    private final StrategyGiftService giftService;

    @Override
    public boolean save(Long marketingStrategyId, List<SaveStrategyCycleLadderRequest> requestList, Long opUserId, Date opTime) {
        // 1.新增和修改的阶梯
        List<Long> idList = new ArrayList<>();
        for (SaveStrategyCycleLadderRequest cycleLadderRequest : requestList) {
            if (Objects.isNull(cycleLadderRequest)) {
                continue;
            }
            StrategyCycleLadderDO cycleLadderDO = PojoUtils.map(cycleLadderRequest, StrategyCycleLadderDO.class);
            cycleLadderDO.setOpUserId(opUserId);
            cycleLadderDO.setOpTime(opTime);
            if (Objects.nonNull(cycleLadderRequest.getId())) {
                this.updateById(cycleLadderDO);
            } else {
                cycleLadderDO.setMarketingStrategyId(marketingStrategyId);
                this.save(cycleLadderDO);
            }
            List<SaveStrategyGiftRequest> strategyGiftList = cycleLadderRequest.getStrategyGiftList();
            giftService.save(marketingStrategyId, cycleLadderDO.getId(), strategyGiftList, opUserId, opTime);
            idList.add(cycleLadderDO.getId());
        }

        // 2.删除删掉的阶梯
        List<StrategyCycleLadderDO> cycleLadderDOList = this.listCycleLadderByActivityId(marketingStrategyId);
        List<StrategyCycleLadderDO> deleteDOList = cycleLadderDOList.stream().filter(e -> !idList.contains(e.getId())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(deleteDOList)) {
            List<Long> deleteIdList = deleteDOList.stream().map(StrategyCycleLadderDO::getId).collect(Collectors.toList());
            StrategyCycleLadderDO cycleLadderDO = new StrategyCycleLadderDO();
            cycleLadderDO.setDelFlag(1);
            cycleLadderDO.setOpUserId(opUserId);
            cycleLadderDO.setOpTime(opTime);
            QueryWrapper<StrategyCycleLadderDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(StrategyCycleLadderDO::getId, deleteIdList);
            this.batchDeleteWithFill(cycleLadderDO, wrapper);
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

    @Override
    public void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long opUserId, Date opTime) {
        List<StrategyCycleLadderDO> strategyCycleLadderDOList = this.listCycleLadderByActivityId(oldId);
        for (StrategyCycleLadderDO strategyCycleLadderDO : strategyCycleLadderDOList) {
            Long oldLadderId = strategyCycleLadderDO.getId();
            strategyCycleLadderDO.setId(null);
            strategyCycleLadderDO.setMarketingStrategyId(strategyActivityDO.getId());
            strategyCycleLadderDO.setOpUserId(opUserId);
            strategyCycleLadderDO.setOpTime(opTime);
            this.save(strategyCycleLadderDO);

            giftService.copy(strategyActivityDO, oldId, strategyCycleLadderDO.getId(), oldLadderId, opUserId, opTime);
        }
    }

    @Override
    public List<StrategyCycleLadderDO> listCycleLadderByActivityId(Long strategyActivityId) {
        QueryWrapper<StrategyCycleLadderDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyCycleLadderDO::getMarketingStrategyId, strategyActivityId);
        return this.list(wrapper);
    }
}
