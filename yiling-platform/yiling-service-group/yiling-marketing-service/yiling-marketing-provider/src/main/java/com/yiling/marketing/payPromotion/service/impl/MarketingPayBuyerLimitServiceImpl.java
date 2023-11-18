package com.yiling.marketing.payPromotion.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.marketing.payPromotion.entity.MarketingPayBuyerLimitDO;
import com.yiling.marketing.payPromotion.dao.MarketingPayBuyerLimitMapper;
import com.yiling.marketing.payPromotion.service.MarketingPayBuyerLimitService;
import com.yiling.framework.common.base.BaseServiceImpl;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付促销客户表 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
@Service
public class MarketingPayBuyerLimitServiceImpl extends BaseServiceImpl<MarketingPayBuyerLimitMapper, MarketingPayBuyerLimitDO> implements MarketingPayBuyerLimitService {

    @Override
    public List<MarketingPayBuyerLimitDO> listByActivityIdListAndEid(List<Long> activityIdList, Long buyerEid) {
        QueryWrapper<MarketingPayBuyerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(MarketingPayBuyerLimitDO::getMarketingPayId, activityIdList);
        wrapper.lambda().eq(MarketingPayBuyerLimitDO::getEid, buyerEid);
        return this.list(wrapper);
    }

    @Override
    public List<MarketingPayBuyerLimitDO> listByActivityIdList(List<Long> activityIdList) {
        QueryWrapper<MarketingPayBuyerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(MarketingPayBuyerLimitDO::getMarketingPayId, activityIdList);
        return this.list(wrapper);
    }
}
