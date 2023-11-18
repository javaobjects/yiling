package com.yiling.marketing.promotion.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.promotion.dto.request.PromotionActivityStatusRequest;
import com.yiling.marketing.promotion.entity.PromotionCombinationPackageDO;

/**
 * <p>
 * 促销活动组合包表 服务类
 * </p>
 *
 * @author shixing.sun
 * @date 2022-04-27
 */
public interface PromotionCombinationPackageService extends BaseService<PromotionCombinationPackageDO> {

    /**
     * 根据活动id查询组合包信息
     * @param promotionActivityId
     * @return
     */
    PromotionCombinationPackageDO queryByPromotionActivityId(Long promotionActivityId);

    /**
     * 复制组合包
     * @param request
     * @param promotionActivityId
     */
    void copy(PromotionActivityStatusRequest request, Long promotionActivityId);
}
