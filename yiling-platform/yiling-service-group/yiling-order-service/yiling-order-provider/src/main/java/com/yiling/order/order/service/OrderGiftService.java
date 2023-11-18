package com.yiling.order.order.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.entity.OrderGiftDO;

/**
 * <p>
 * 订单赠品表 服务类
 * </p>
 *
 * @author zhigang.guo
 * @date 2021-10-23
 */
public interface OrderGiftService extends BaseService<OrderGiftDO> {
    /**
     * 根据订单id查询
     * @param orderId
     * @return
     */
    List<OrderGiftDO> listByOrderId(Long orderId);

    /**
     * 根据订单ID,活动ID查询赠品信息
     * @param orderId 订单ID
     * @param activityId 活动ID
     * @return
     */
    List<OrderGiftDO> listByOrderId(Long orderId,Long activityId);

    /**
     * 根据订单ids查询
     * @param orderIds
     * @return
     */
    List<OrderGiftDO> listByOrderIds(List<Long> orderIds);
}
