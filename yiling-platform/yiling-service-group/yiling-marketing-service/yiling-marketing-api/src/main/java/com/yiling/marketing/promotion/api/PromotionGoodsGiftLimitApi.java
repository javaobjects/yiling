package com.yiling.marketing.promotion.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftUsedDTO;
import com.yiling.marketing.promotion.dto.request.PromotionGoodsGiftUsedRequest;

/**
 * 促销活动赠品Api
 *
 * @author:wei.wang
 * @date:2021/11/8
 */
public interface PromotionGoodsGiftLimitApi {
    /**
     * 根据促销活动查询
     *
     * @param promotionActivityIdList 促销活动id
     * @return
     */
    List<PromotionGoodsGiftLimitDTO> listByActivityId(List<Long> promotionActivityIdList);

    /**
     * 查询参与满赠活动的订单信息
     *
     * @param request
     * @return
     */
    Page<PromotionGoodsGiftUsedDTO> pageGiftOrder(PromotionGoodsGiftUsedRequest request);
}
