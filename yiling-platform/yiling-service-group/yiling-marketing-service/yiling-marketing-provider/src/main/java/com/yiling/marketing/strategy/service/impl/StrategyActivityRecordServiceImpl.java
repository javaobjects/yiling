package com.yiling.marketing.strategy.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.strategy.dao.StrategyActivityRecordMapper;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityRecordListRequest;
import com.yiling.marketing.strategy.dto.request.QueryStrategyActivityRecordPageRequest;
import com.yiling.marketing.strategy.entity.StrategyActivityRecordDO;
import com.yiling.marketing.strategy.service.StrategyActivityRecordService;

/**
 * <p>
 * 营销活动记录表 服务实现类
 * </p>
 *
 * @author zhangy
 * @date 2022-09-06
 */
@Service
public class StrategyActivityRecordServiceImpl extends BaseServiceImpl<StrategyActivityRecordMapper, StrategyActivityRecordDO> implements StrategyActivityRecordService {

    @Override
    public Page<StrategyActivityRecordDO> pageList(QueryStrategyActivityRecordPageRequest request) {
        QueryWrapper<StrategyActivityRecordDO> wrapper = new QueryWrapper<>();
        if (Objects.nonNull(request.getMarketingStrategyId())) {
            wrapper.lambda().eq(StrategyActivityRecordDO::getMarketingStrategyId, request.getMarketingStrategyId());
        }
        if (Objects.nonNull(request.getStrategyType()) && 0 != request.getStrategyType()) {
            wrapper.lambda().eq(StrategyActivityRecordDO::getStrategyType, request.getStrategyType());
        }
        Page<StrategyActivityRecordDO> objectPage = new Page<>(request.getCurrent(), request.getSize());
        return this.page(objectPage, wrapper);
    }

    @Override
    public Integer countRecordByActivityId(Long strategyActivityId) {
        QueryWrapper<StrategyActivityRecordDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyActivityRecordDO::getMarketingStrategyId, strategyActivityId);
        return this.count(wrapper);
    }

    @Override
    public Integer countRecordByActivityIdAndEid(Long strategyActivityId, Long ladderId, Long eid, Long orderId) {
        QueryWrapper<StrategyActivityRecordDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(StrategyActivityRecordDO::getMarketingStrategyId, strategyActivityId);
        wrapper.lambda().eq(StrategyActivityRecordDO::getEid, eid);
        if (Objects.nonNull(ladderId)) {
            wrapper.lambda().eq(StrategyActivityRecordDO::getLadderId, ladderId);
        }
        if (Objects.nonNull(orderId)) {
            wrapper.lambda().eq(StrategyActivityRecordDO::getOrderId, orderId);
        }
        return this.count(wrapper);
    }

    @Override
    public List<StrategyActivityRecordDO> listByCondition(QueryStrategyActivityRecordListRequest request) {
        QueryWrapper<StrategyActivityRecordDO> wrapper = new QueryWrapper<>();
        if (Objects.nonNull(request.getMarketingStrategyId())) {
            wrapper.lambda().eq(StrategyActivityRecordDO::getMarketingStrategyId, request.getMarketingStrategyId());
        }
        if (Objects.nonNull(request.getStrategyType())) {
            wrapper.lambda().eq(StrategyActivityRecordDO::getStrategyType, request.getStrategyType());
        }
        if (Objects.nonNull(request.getOrderId())) {
            wrapper.lambda().eq(StrategyActivityRecordDO::getOrderId, request.getOrderId());
        }
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(StrategyActivityRecordDO::getEid, request.getEid());
        }
        if (Objects.nonNull(request.getLadderId())) {
            wrapper.lambda().eq(StrategyActivityRecordDO::getLadderId, request.getLadderId());
        }
        if (Objects.nonNull(request.getStartTime())) {
            wrapper.lambda().ge(StrategyActivityRecordDO::getCreateTime, request.getStartTime());
        }
        if (Objects.nonNull(request.getStopTime())) {
            wrapper.lambda().le(StrategyActivityRecordDO::getCreateTime, request.getStopTime());
        }
        return this.list(wrapper);
    }

    @Override
    public StrategyActivityRecordDO getFirstByCondition(QueryStrategyActivityRecordListRequest request) {
        QueryWrapper<StrategyActivityRecordDO> wrapper = new QueryWrapper<>();
        if (Objects.nonNull(request.getMarketingStrategyId())) {
            wrapper.lambda().eq(StrategyActivityRecordDO::getMarketingStrategyId, request.getMarketingStrategyId());
        }
        if (Objects.nonNull(request.getStrategyType())) {
            wrapper.lambda().eq(StrategyActivityRecordDO::getStrategyType, request.getStrategyType());
        }
        if (Objects.nonNull(request.getOrderId())) {
            wrapper.lambda().eq(StrategyActivityRecordDO::getOrderId, request.getOrderId());
        }
        if (Objects.nonNull(request.getEid())) {
            wrapper.lambda().eq(StrategyActivityRecordDO::getEid, request.getEid());
        }
        if (Objects.nonNull(request.getLadderId())) {
            wrapper.lambda().eq(StrategyActivityRecordDO::getLadderId, request.getLadderId());
        }
        if (Objects.nonNull(request.getStartTime())) {
            wrapper.lambda().ge(StrategyActivityRecordDO::getCreateTime, request.getStartTime());
        }
        if (Objects.nonNull(request.getStopTime())) {
            wrapper.lambda().le(StrategyActivityRecordDO::getCreateTime, request.getStopTime());
        }
        wrapper.lambda().last(" limit 1");
        return this.getOne(wrapper);
    }
}
