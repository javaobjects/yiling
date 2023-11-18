package com.yiling.marketing.payPromotion.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.payPromotion.entity.MarketingPayEnterpriseGoodsLimitDO;

/**
 * <p>
 * 支付促销店铺SKU 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
public interface MarketingPayEnterpriseGoodsLimitService extends BaseService<MarketingPayEnterpriseGoodsLimitDO> {


    /**
     * 根据活动id和商品id集合查询指定店铺SKU
     *
     * @param activityId 活动id
     * @param goodsIdList 商品id集合
     * @return 指定店铺SKU
     */
    List<MarketingPayEnterpriseGoodsLimitDO> listByActivityIdAndGoodsIdList(Long activityId, List<Long> goodsIdList);

    /**
     * 根据活动id和商品id集合查询指定店铺SKU
     *
     * @param activityId 活动id
     * @return 指定店铺SKU
     */
    List<MarketingPayEnterpriseGoodsLimitDO> listByActivityIdIdList(Long activityId);

}
