package com.yiling.marketing.payPromotion.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromotionSellerLimitDO;

/**
 * <p>
 * 支付促销商家表 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
public interface MarketingPayPromotionSellerLimitService extends BaseService<MarketingPayPromotionSellerLimitDO> {

    /**
     * 根据活动id查询商家表
     *
     * @param activityId 活动id
     * @return 活动商家信息
     */
    List<MarketingPayPromotionSellerLimitDO> listByActivityId(Long activityId);


    MarketingPayPromotionSellerLimitDO getByActivityIdAndSellerEid(Long activityId, Long sellerEid);

}
