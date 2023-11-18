package com.yiling.marketing.payPromotion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.payPromotion.dao.MarketingPayPromotionSellerLimitMapper;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromotionSellerLimitDO;
import com.yiling.marketing.payPromotion.service.MarketingPayPromotionSellerLimitService;

/**
 * <p>
 * 支付促销商家表 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
@Service
public class MarketingPayPromotionSellerLimitServiceImpl extends BaseServiceImpl<MarketingPayPromotionSellerLimitMapper, MarketingPayPromotionSellerLimitDO> implements MarketingPayPromotionSellerLimitService {

    @Override
    public List<MarketingPayPromotionSellerLimitDO> listByActivityId(Long activityId) {
        QueryWrapper<MarketingPayPromotionSellerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayPromotionSellerLimitDO::getMarketingPayId, activityId);
        return this.list(wrapper);
    }

    @Override
    public MarketingPayPromotionSellerLimitDO getByActivityIdAndSellerEid(Long activityId, Long sellerEid) {
        QueryWrapper<MarketingPayPromotionSellerLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayPromotionSellerLimitDO::getMarketingPayId, activityId);
        wrapper.lambda().eq(MarketingPayPromotionSellerLimitDO::getEid, sellerEid);
        wrapper.lambda().last(" limit 1");
        return this.getOne(wrapper);
    }
}
