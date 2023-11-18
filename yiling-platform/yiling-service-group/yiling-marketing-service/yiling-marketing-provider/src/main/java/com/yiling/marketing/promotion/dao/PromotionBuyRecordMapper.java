package com.yiling.marketing.promotion.dao;

import com.yiling.framework.common.base.BaseMapper;
import com.yiling.marketing.promotion.dto.request.PromotionUpdateBuyRecordRequest;
import com.yiling.marketing.promotion.entity.PromotionBuyRecordDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 促销活动购买记录表
 *
 * @author fan.shen
 * @date 2022-2-8
 */
@Repository
public interface PromotionBuyRecordMapper extends BaseMapper<PromotionBuyRecordDO> {

    /**
     * 退货更新购买数量
     * @param orderId
     * @param goodsId
     * @param quantity
     * @return
     */
    int updateBuyRecordQuantity(@Param("orderId") Long orderId,@Param("goodsId") Long goodsId, @Param("quantity") Integer quantity);
}
