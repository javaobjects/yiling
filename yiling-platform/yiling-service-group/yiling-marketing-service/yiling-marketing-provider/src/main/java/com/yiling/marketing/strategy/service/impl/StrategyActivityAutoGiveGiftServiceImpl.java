package com.yiling.marketing.strategy.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.strategy.dao.StrategyActivityAutoGiveGiftMapper;
import com.yiling.marketing.strategy.dto.StrategyGiftDTO;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityAutoGiveGiftPageRequest;
import com.yiling.marketing.strategy.dto.request.SaveActivityAutoGiveGiftRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityAutoGiveGiftDO;
import com.yiling.marketing.strategy.service.StrategyActivityAutoGiveGiftService;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 策略满赠续费会员自动赠送赠品表 服务实现类
 * </p>
 *
 * @author zhangy
 * @date 2022-09-20
 */
@Service
public class StrategyActivityAutoGiveGiftServiceImpl extends BaseServiceImpl<StrategyActivityAutoGiveGiftMapper, StrategyActivityAutoGiveGiftDO> implements StrategyActivityAutoGiveGiftService {

    @Override
    public Boolean saveList(SaveActivityAutoGiveGiftRequest request) {
        if (CollUtil.isEmpty(request.getStrategyGiftList())) {
            return false;
        }
        List<StrategyActivityAutoGiveGiftDO> activityAutoGiveGiftDOList = new ArrayList<>();
        for (StrategyGiftDTO strategyGiftDTO : request.getStrategyGiftList()) {
            StrategyActivityAutoGiveGiftDO giveGiftDO = new StrategyActivityAutoGiveGiftDO();
            giveGiftDO.setMarketingStrategyId(request.getMarketingStrategyId());
            giveGiftDO.setOrderId(request.getOrderId());
            giveGiftDO.setEid(request.getEid());
            giveGiftDO.setMemberId(request.getMemberId());
            giveGiftDO.setGiveTime(request.getGiveTime());
            giveGiftDO.setStartTime(request.getStartTime());
            giveGiftDO.setType(strategyGiftDTO.getType());
            giveGiftDO.setGiftId(strategyGiftDTO.getGiftId());
            giveGiftDO.setCount(strategyGiftDTO.getCount());
            activityAutoGiveGiftDOList.add(giveGiftDO);
        }
        return this.saveBatch(activityAutoGiveGiftDOList);
    }

    @Override
    public Page<StrategyActivityAutoGiveGiftDO> pageByCondition(QueryStrategyActivityAutoGiveGiftPageRequest request) {
        QueryWrapper<StrategyActivityAutoGiveGiftDO> wrapper = new QueryWrapper<>();
        if (Objects.nonNull(request.getMarketingStrategyId())) {
            wrapper.lambda().eq(StrategyActivityAutoGiveGiftDO::getMarketingStrategyId, request.getMarketingStrategyId());
        }
        if (Objects.nonNull(request.getOrderId())) {
            wrapper.lambda().eq(StrategyActivityAutoGiveGiftDO::getOrderId, request.getOrderId());
        }
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(StrategyActivityAutoGiveGiftDO::getEid, request.getEid());
        }
        if (Objects.nonNull(request.getMemberId())) {
            wrapper.lambda().eq(StrategyActivityAutoGiveGiftDO::getMemberId, request.getMemberId());
        }
        if (StringUtils.isNotBlank(request.getGiveTime())) {
            wrapper.lambda().eq(StrategyActivityAutoGiveGiftDO::getGiveTime, request.getGiveTime());
        }
        if (CollUtil.isNotEmpty(request.getTypeList())) {
            wrapper.lambda().in(StrategyActivityAutoGiveGiftDO::getType, request.getTypeList());
        }
        if (Objects.nonNull(request.getGiftId())) {
            wrapper.lambda().eq(StrategyActivityAutoGiveGiftDO::getGiftId, request.getGiftId());
        }
        Page<StrategyActivityAutoGiveGiftDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return this.page(objectPage, wrapper);
    }

    @Override
    public List<StrategyActivityAutoGiveGiftDO> listByCondition(QueryStrategyActivityAutoGiveGiftPageRequest request) {
        QueryWrapper<StrategyActivityAutoGiveGiftDO> wrapper = new QueryWrapper<>();
        if (Objects.nonNull(request.getMarketingStrategyId())) {
            wrapper.lambda().eq(StrategyActivityAutoGiveGiftDO::getMarketingStrategyId, request.getMarketingStrategyId());
        }
        if (Objects.nonNull(request.getOrderId())) {
            wrapper.lambda().eq(StrategyActivityAutoGiveGiftDO::getOrderId, request.getOrderId());
        }
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(StrategyActivityAutoGiveGiftDO::getEid, request.getEid());
        }
        if (Objects.nonNull(request.getMemberId())) {
            wrapper.lambda().eq(StrategyActivityAutoGiveGiftDO::getMemberId, request.getMemberId());
        }
        if (StringUtils.isNotBlank(request.getGiveTime())) {
            wrapper.lambda().eq(StrategyActivityAutoGiveGiftDO::getGiveTime, request.getGiveTime());
        }
        if (CollUtil.isNotEmpty(request.getTypeList())) {
            wrapper.lambda().in(StrategyActivityAutoGiveGiftDO::getType, request.getTypeList());
        }
        if (Objects.nonNull(request.getGiftId())) {
            wrapper.lambda().eq(StrategyActivityAutoGiveGiftDO::getGiftId, request.getGiftId());
        }
        if (Objects.nonNull(request.getStartTime())) {
            wrapper.lambda().le(StrategyActivityAutoGiveGiftDO::getStartTime, request.getStartTime());
        }
        return this.list(wrapper);
    }

    @Override
    public Boolean deleteByIdList(List<Long> idList, Long opUserId) {
        StrategyActivityAutoGiveGiftDO strategyActivityAutoGiveGiftDO = new StrategyActivityAutoGiveGiftDO();
        strategyActivityAutoGiveGiftDO.setDelFlag(1);
        strategyActivityAutoGiveGiftDO.setOpUserId(opUserId);
        strategyActivityAutoGiveGiftDO.setOpTime(new Date());
        QueryWrapper<StrategyActivityAutoGiveGiftDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(StrategyActivityAutoGiveGiftDO::getId, idList);
        this.batchDeleteWithFill(strategyActivityAutoGiveGiftDO, wrapper);
        return true;
    }
}
