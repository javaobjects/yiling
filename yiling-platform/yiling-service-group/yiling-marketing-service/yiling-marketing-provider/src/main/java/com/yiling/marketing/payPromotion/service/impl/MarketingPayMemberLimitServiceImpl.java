package com.yiling.marketing.payPromotion.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.marketing.payPromotion.dao.MarketingPayMemberLimitMapper;
import com.yiling.marketing.payPromotion.entity.MarketingPayMemberLimitDO;
import com.yiling.marketing.payPromotion.service.MarketingPayMemberLimitService;

/**
 * <p>
 * 支付促销会员方案表 服务实现类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
@Service
public class MarketingPayMemberLimitServiceImpl extends BaseServiceImpl<MarketingPayMemberLimitMapper, MarketingPayMemberLimitDO> implements MarketingPayMemberLimitService {

    @Override
    public List<MarketingPayMemberLimitDO> listMemberByActivityId(Long activityId) {
        QueryWrapper<MarketingPayMemberLimitDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MarketingPayMemberLimitDO::getMarketingPayId, activityId);
        return this.list(wrapper);
    }
}
