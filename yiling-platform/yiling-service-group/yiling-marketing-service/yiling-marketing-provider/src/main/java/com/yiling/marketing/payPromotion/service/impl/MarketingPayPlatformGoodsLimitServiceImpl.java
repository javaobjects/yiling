package com.yiling.marketing.payPromotion.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.marketing.payPromotion.entity.MarketingPayPlatformGoodsLimitDO;
import com.yiling.marketing.payPromotion.dao.MarketingPayPlatformGoodsLimitMapper;
import com.yiling.marketing.payPromotion.service.MarketingPayPlatformGoodsLimitService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.strategy.entity.StrategyEnterpriseGoodsLimitDO;

import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 支付促销平台SKU表 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
@Service
public class MarketingPayPlatformGoodsLimitServiceImpl extends BaseServiceImpl<MarketingPayPlatformGoodsLimitMapper, MarketingPayPlatformGoodsLimitDO> implements MarketingPayPlatformGoodsLimitService {

    @Override
    public List<MarketingPayPlatformGoodsLimitDO> listByActivityIdAndSellSpecificationsIdList(Long activityId, List<Long> sellSpecificationsIdList) {
        QueryWrapper<MarketingPayPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayPlatformGoodsLimitDO::getMarketingPayId, activityId);
        if (CollUtil.isNotEmpty(sellSpecificationsIdList)) {
            wrapper.lambda().in(MarketingPayPlatformGoodsLimitDO::getSellSpecificationsId, sellSpecificationsIdList);
        }
        return this.list(wrapper);
    }

    @Override
    public List<MarketingPayPlatformGoodsLimitDO> listByActivityId(Long activityId) {
        QueryWrapper<MarketingPayPlatformGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayPlatformGoodsLimitDO::getMarketingPayId, activityId);
        return this.list(wrapper);
    }
}
