package com.yiling.marketing.payPromotion.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.payPromotion.entity.MarketingPayPromoterMemberLimitDO;

/**
 * <p>
 * 支付促销推广方会员表 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-09-21
 */
public interface MarketingPayPromoterMemberLimitService extends BaseService<MarketingPayPromoterMemberLimitDO> {


    /**
     * 根据活动id查询推广方会员商家
     *
     * @param activityId 活动id
     * @return 推广方会员商家信息
     */
    List<MarketingPayPromoterMemberLimitDO> listByActivityIdAndEidList(Long activityId);

}
