package com.yiling.marketing.payPromotion.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.payPromotion.entity.MarketingPayMemberLimitDO;

/**
 * <p>
 * 支付促销会员方案表 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
public interface MarketingPayMemberLimitService extends BaseService<MarketingPayMemberLimitDO> {

    /**
     * 根据活动id查询指定方案
     *
     * @param activityId 活动id
     * @return 指定方案
     */
    List<MarketingPayMemberLimitDO> listMemberByActivityId(Long activityId);
}
