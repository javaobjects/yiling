package com.yiling.marketing.promotion.service;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.promotion.dto.request.PromotionActivityStatusRequest;
import com.yiling.marketing.promotion.entity.PromotionEnterpriseLimitDO;

/**
 * <p>
 * 促销活动企业限制表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface PromotionEnterpriseLimitService extends BaseService<PromotionEnterpriseLimitDO> {

    List<PromotionEnterpriseLimitDO> queryByActivityId(Long promotionActivityId);

    boolean editEnterprise(List<PromotionEnterpriseLimitDO> enterpriseLimitDOS, Long promotionActivityId, Long opUserId, Date opTime);

    /**
     * 复制
     *
     * @param request
     * @return
     */
    boolean copy(PromotionActivityStatusRequest request, Long promotionActivityId);
}
