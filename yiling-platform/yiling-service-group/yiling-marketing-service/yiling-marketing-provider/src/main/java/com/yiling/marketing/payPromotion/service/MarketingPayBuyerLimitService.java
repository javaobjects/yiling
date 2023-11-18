package com.yiling.marketing.payPromotion.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.payPromotion.entity.MarketingPayBuyerLimitDO;

/**
 * <p>
 * 支付促销客户表 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
public interface MarketingPayBuyerLimitService extends BaseService<MarketingPayBuyerLimitDO> {


    /**
     * 根据活动id集合和企业id查询指定商户信息
     *
     * @param activityIdList 活动id集合
     * @param buyerEid 企业id
     * @return 指定客户
     */
    List<MarketingPayBuyerLimitDO> listByActivityIdListAndEid(List<Long> activityIdList, Long buyerEid);

    /**
     * 根据活动id集合和企业id查询指定商户信息
     *
     * @param activityIdList 活动id集合
     * @return 指定客户
     */
    List<MarketingPayBuyerLimitDO> listByActivityIdList(List<Long> activityIdList);
}
