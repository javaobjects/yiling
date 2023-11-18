package com.yiling.marketing.promotion.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftUsedDTO;
import com.yiling.marketing.promotion.dto.request.PromotionGoodsGiftUsedRequest;
import com.yiling.marketing.promotion.entity.PromotionGoodsGiftLimitDO;

/**
 * <p>
 * 促销活动赠品表 Dao 接口
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
@Repository
public interface PromotionGoodsGiftLimitMapper extends BaseMapper<PromotionGoodsGiftLimitDO> {

    /**
     * 活动参与数量(赠品扣减)
     *
     * @param activityId
     * @param reduceQuality
     * @return
     */
    int reducePromotionGoodsGift(@Param("activityId") Long activityId,@Param("goodsGiftId") Long goodsGiftId, @Param("reduceQuality") Integer reduceQuality);

    /**
     * 查询参与满赠活动的订单信息
     *
     * @param page
     * @param request
     * @return
     */
    Page<PromotionGoodsGiftUsedDTO> pageGiftOrder(Page<PromotionGoodsGiftLimitDO> page, @Param("request") PromotionGoodsGiftUsedRequest request);

    /**
     * 减少满赠活动赠品的总数量
     *
     * @param id
     * @param reduceQuality
     * @param opUserId
     * @param opTime
     * @return
     */
    int reduceActivityGift(@Param("id") Long id, @Param("reduceQuality") Integer reduceQuality, @Param("opUserId") Long opUserId, @Param("opTime") Date opTime);
}
