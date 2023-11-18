package com.yiling.order.order.dao;

import com.yiling.order.order.dto.request.SumOrderPromotionActivityRequest;
import com.yiling.order.order.entity.OrderPromotionActivityDO;
import com.yiling.framework.common.base.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 订单与促销活动关联表 Dao 接口
 * </p>
 *
 * @author zhigang.guo
 * @date 2022-02-16
 */
@Repository
public interface OrderPromotionActivityMapper extends BaseMapper<OrderPromotionActivityDO> {

    /**
     * 查询订单组合活动数据
     * @param request
     * @return
     */
    Long sumOrderPromotionActivity(@Param("request") SumOrderPromotionActivityRequest request);

}
