package com.yiling.marketing.payPromotion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.payPromotion.dao.MarketingPayEnterpriseGoodsLimitMapper;
import com.yiling.marketing.payPromotion.entity.MarketingPayEnterpriseGoodsLimitDO;
import com.yiling.marketing.payPromotion.service.MarketingPayEnterpriseGoodsLimitService;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * 支付促销店铺SKU 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
@Service
public class MarketingPayEnterpriseGoodsLimitServiceImpl extends BaseServiceImpl<MarketingPayEnterpriseGoodsLimitMapper, MarketingPayEnterpriseGoodsLimitDO> implements MarketingPayEnterpriseGoodsLimitService {

    @Override
    public List<MarketingPayEnterpriseGoodsLimitDO> listByActivityIdAndGoodsIdList(Long activityId, List<Long> goodsIdList) {
        QueryWrapper<MarketingPayEnterpriseGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getMarketingPayId, activityId);
        if (CollUtil.isNotEmpty(goodsIdList)) {
            wrapper.lambda().in(MarketingPayEnterpriseGoodsLimitDO::getGoodsId, goodsIdList);
        }
        return this.list(wrapper);
    }

    @Override
    public List<MarketingPayEnterpriseGoodsLimitDO> listByActivityIdIdList(Long activityId) {
        QueryWrapper<MarketingPayEnterpriseGoodsLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayEnterpriseGoodsLimitDO::getMarketingPayId, activityId);
        return this.list(wrapper);
    }
}
