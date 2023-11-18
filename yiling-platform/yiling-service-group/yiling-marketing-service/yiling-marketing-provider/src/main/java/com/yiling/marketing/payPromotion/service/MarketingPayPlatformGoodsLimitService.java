package com.yiling.marketing.payPromotion.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.payPromotion.entity.MarketingPayPlatformGoodsLimitDO;

/**
 * <p>
 * 支付促销平台SKU表 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
public interface MarketingPayPlatformGoodsLimitService extends BaseService<MarketingPayPlatformGoodsLimitDO> {


    /**
     * 根据活动id和商品标准库ID集合查询指定平台SKU
     *
     * @param activityId 活动id
     * @param sellSpecificationsIdList 规格ID集合
     * @return 指定平台SKU
     */
    List<MarketingPayPlatformGoodsLimitDO> listByActivityIdAndSellSpecificationsIdList(Long activityId, List<Long> sellSpecificationsIdList);

    /**
     * 根据活动id和商品标准库ID集合查询指定平台SKU
     *
     * @param activityId 活动id
     * @return 指定平台SKU
     */
    List<MarketingPayPlatformGoodsLimitDO> listByActivityId(Long activityId);

}
