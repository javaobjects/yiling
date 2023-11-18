package com.yiling.marketing.promotion.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.promotion.api.PromotionGoodsGiftLimitApi;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftUsedDTO;
import com.yiling.marketing.promotion.dto.request.PromotionGoodsGiftUsedRequest;
import com.yiling.marketing.promotion.entity.PromotionGoodsGiftLimitDO;
import com.yiling.marketing.promotion.service.PromotionGoodsGiftLimitService;

/**
 * 促销活动赠品Api
 *
 * @author:wei.wang
 * @date:2021/11/8
 */
@DubboService
public class PromotionGoodsGiftLimitApiImpl implements PromotionGoodsGiftLimitApi {

    @Autowired
    private PromotionGoodsGiftLimitService promotionGoodsGiftLimitService;

    /**
     * 根据促销活动查询
     *
     * @param promotionActivityIdList 促销活动id
     * @return
     */
    @Override
    public List<PromotionGoodsGiftLimitDTO> listByActivityId(List<Long> promotionActivityIdList) {
        List<PromotionGoodsGiftLimitDO> promotionGoodsGiftLimitList = promotionGoodsGiftLimitService.listByActivityId(promotionActivityIdList);
        return PojoUtils.map(promotionGoodsGiftLimitList, PromotionGoodsGiftLimitDTO.class);
    }

    @Override
    public Page<PromotionGoodsGiftUsedDTO> pageGiftOrder(PromotionGoodsGiftUsedRequest request) {
        return promotionGoodsGiftLimitService.pageGiftOrder(request);
    }
}
