package com.yiling.marketing.strategy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.strategy.dao.StrategyGiftMapper;
import com.yiling.marketing.strategy.dto.request.SaveStrategyGiftRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityDO;
import com.yiling.marketing.strategy.entity.StrategyGiftDO;
import com.yiling.marketing.strategy.service.StrategyGiftService;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 营销活动赠品表 服务实现类
 * </p>
 *
 * @author zhangy
 * @date 2022-08-22
 */
@Service
public class StrategyGiftServiceImpl extends BaseServiceImpl<StrategyGiftMapper, StrategyGiftDO> implements StrategyGiftService {

    @Override
    public boolean save(Long marketingStrategyId, Long ladderId, List<SaveStrategyGiftRequest> requestList, Long opUserId, Date opTime) {
        // 1.新增和修改
        List<Long> idList = new ArrayList<>();
        for (SaveStrategyGiftRequest giftRequest : requestList) {
            if (Objects.isNull(giftRequest)) {
                continue;
            }
            StrategyGiftDO giftDO = PojoUtils.map(giftRequest, StrategyGiftDO.class);
            giftDO.setOpUserId(opUserId);
            giftDO.setOpTime(opTime);
            if (Objects.nonNull(giftRequest.getId())) {
                this.updateById(giftDO);
            } else {
                giftDO.setMarketingStrategyId(marketingStrategyId);
                giftDO.setLadderId(ladderId);
                this.save(giftDO);
            }
            idList.add(giftDO.getId());
        }
        // 2.删除赠品
        List<StrategyGiftDO> giftDOList = this.listGiftByActivityIdAndLadderId(marketingStrategyId, ladderId);
        List<StrategyGiftDO> deleteDOList = giftDOList.stream().filter(e -> !idList.contains(e.getId())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(deleteDOList)) {
            List<Long> deleteIdList = deleteDOList.stream().map(StrategyGiftDO::getId).collect(Collectors.toList());
            StrategyGiftDO giftDO = new StrategyGiftDO();
            giftDO.setDelFlag(1);
            giftDO.setOpUserId(opUserId);
            giftDO.setOpTime(opTime);
            QueryWrapper<StrategyGiftDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().in(StrategyGiftDO::getId, deleteIdList);
            this.batchDeleteWithFill(giftDO, wrapper);
        }
        return true;
    }

    @Override
    public void copy(StrategyActivityDO strategyActivityDO, Long oldId, Long ladderId, Long oldLadderId, Long opUserId, Date opTime) {
        List<StrategyGiftDO> strategyGiftDOList = this.listGiftByActivityIdAndLadderId(oldId, oldLadderId);
        for (StrategyGiftDO giftDO : strategyGiftDOList) {
            giftDO.setId(null);
            giftDO.setMarketingStrategyId(strategyActivityDO.getId());
            giftDO.setLadderId(ladderId);
            giftDO.setOpUserId(opUserId);
            giftDO.setOpTime(opTime);
        }
        this.saveBatch(strategyGiftDOList);
    }

    @Override
    public List<StrategyGiftDO> listGiftByActivityIdAndLadderId(Long strategyActivityId, Long ladderId) {
        QueryWrapper<StrategyGiftDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyGiftDO::getMarketingStrategyId, strategyActivityId);
        if (Objects.nonNull(ladderId)) {
            wrapper.lambda().eq(StrategyGiftDO::getLadderId, ladderId);
        }
        return this.list(wrapper);
    }
}
