package com.yiling.marketing.payPromotion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.payPromotion.dao.MarketingPayPromoterMemberLimitMapper;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromoterMemberLimitDO;
import com.yiling.marketing.payPromotion.service.MarketingPayPromoterMemberLimitService;

/**
 * <p>
 * 支付促销推广方会员表 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
@Service
public class MarketingPayPromoterMemberLimitServiceImpl extends BaseServiceImpl<MarketingPayPromoterMemberLimitMapper, MarketingPayPromoterMemberLimitDO> implements MarketingPayPromoterMemberLimitService {

    @Override
    public List<MarketingPayPromoterMemberLimitDO> listByActivityIdAndEidList(Long activityId) {
        QueryWrapper<MarketingPayPromoterMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayPromoterMemberLimitDO::getMarketingPayId, activityId);
        return this.list(wrapper);
    }
}
