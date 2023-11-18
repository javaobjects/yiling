package com.yiling.marketing.promotion.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.marketing.promotion.dto.request.ActivityGoodsPageRequest;
import com.yiling.marketing.promotion.entity.PromotionGoodsLimitDO;

/**
 * <p>
 * 促销活动商品表 服务类
 * </p>
 *
 * @author houjie.sun
 * @date 2021-10-23
 */
public interface PromotionGoodsLimitService extends BaseService<PromotionGoodsLimitDO> {

    List<PromotionGoodsLimitDO> queryByActivityId(Long promotionActivityId);

    List<PromotionGoodsLimitDO> queryByActivityIdList(List<Long> promotionActivityIdList);

    Page<PromotionGoodsLimitDO> pageGoodsByActivityId(ActivityGoodsPageRequest request);

    List<PromotionGoodsLimitDO> queryByGoodsIdList(List<Long> goodsIdList);

    boolean editGoods(List<PromotionGoodsLimitDO> goodsLimitDOS, Long promotionActivityId, Long opUserId, Date opTime);

}
