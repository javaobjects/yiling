package com.yiling.marketing.promotion.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftLimitDTO;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftUsedDTO;
import com.yiling.marketing.promotion.dto.request.PromotionActivityStatusRequest;
import com.yiling.marketing.promotion.dto.request.PromotionGoodsGiftUsedRequest;
import com.yiling.marketing.promotion.dto.request.PromotionReduceRequest;
import com.yiling.marketing.promotion.dto.request.PromotionSaveRequest;
import com.yiling.marketing.promotion.entity.PromotionGoodsGiftLimitDO;

/**
 * <p>
 * 促销活动赠品表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface PromotionGoodsGiftLimitService extends BaseService<PromotionGoodsGiftLimitDO> {

    /**
     * 扣减赠品库存
     *
     * @param request
     * @return
     */
    boolean reducePromotion(PromotionReduceRequest request);

    List<PromotionGoodsGiftLimitDO> listByActivityIdList(List<Long> promotionActivityIdList);

    List<PromotionGoodsGiftLimitDO> queryByActivityId(Long promotionActivityId);

    /**
     * 根据批量赠品库id查询  启动并且在活动时间内的促销活动
     *
     * @param goodsGiftIdList
     * @return
     */
    List<PromotionGoodsGiftLimitDTO> queryByGiftIdList(List<Long> goodsGiftIdList);

    boolean editGoodsGift(PromotionSaveRequest request);

    /**
     * 根据促销活动查询
     *
     * @param promotionActivityIdList 促销活动id
     * @return
     */
    List<PromotionGoodsGiftLimitDO> listByActivityId(List<Long> promotionActivityIdList);

    /**
     * 复制
     *
     * @param request
     * @return
     */
    boolean copy(PromotionActivityStatusRequest request, Long promotionActivityId);

    /**
     * 查询参与满赠活动的订单信息
     *
     * @param request
     * @return
     */
    Page<PromotionGoodsGiftUsedDTO> pageGiftOrder(PromotionGoodsGiftUsedRequest request);

    /**
     * 减少参与活动商品数量
     *
     * @param id
     * @param quantity
     * @param opUserId
     * @param opTime
     * @return
     */
    boolean reduceActivityQuantity(Long id, Integer quantity, Long opUserId, Date opTime);
}
