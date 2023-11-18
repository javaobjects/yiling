package com.yiling.marketing.couponactivity.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.couponactivity.entity.CouponActivityEnterpriseGetRulesDO;

/**
 * <p>
 * 商家券自主领券规则表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-11-10
 */
public interface CouponActivityEnterpriseGetRulesService extends BaseService<CouponActivityEnterpriseGetRulesDO> {

    /**
     * 根据优惠券活动ID查询 商家券自主领券规则表
     * @param couponActivityId
     * @return
     */
    CouponActivityEnterpriseGetRulesDO getByCouponActivityId(Long couponActivityId);

    /**
     * 根据优惠券活动ID列表查询 商家券自主领券规则表
     * @param couponActivityIdList
     * @return
     */
    List<CouponActivityEnterpriseGetRulesDO> getByCouponActivityIdList(List<Long> couponActivityIdList);

    /**
     * 根据ID删除
     * @param enterpriseGetRules
     * @return
     */
    Integer deleteById(CouponActivityEnterpriseGetRulesDO enterpriseGetRules);

}
