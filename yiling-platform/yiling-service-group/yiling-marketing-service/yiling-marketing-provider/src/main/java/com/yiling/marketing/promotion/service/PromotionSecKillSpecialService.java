package com.yiling.marketing.promotion.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.promotion.dto.request.PromotionActivityStatusRequest;
import com.yiling.marketing.promotion.entity.PromotionSecKillSpecialDO;

/**
 * <p>
 * 促销活动秒杀特价 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-1-14
 */
public interface PromotionSecKillSpecialService extends BaseService<PromotionSecKillSpecialDO> {

    /**
     * 根据活动id查询秒杀&特价信息
     * @param promotionActivityId
     * @return
     */
    PromotionSecKillSpecialDO queryByPromotionActivityId(Long promotionActivityId);

    /**
     * 根据活动id批量查询秒杀&特价信息
     * @param promotionActivityIdList
     * @return
     */
    List<PromotionSecKillSpecialDO> queryByPromotionActivityIdList(List<Long> promotionActivityIdList);

    /**
     * 复制秒杀&特价信息
     * @param request
     * @param promotionActivityId
     */
    void copy(PromotionActivityStatusRequest request, Long promotionActivityId);
}
