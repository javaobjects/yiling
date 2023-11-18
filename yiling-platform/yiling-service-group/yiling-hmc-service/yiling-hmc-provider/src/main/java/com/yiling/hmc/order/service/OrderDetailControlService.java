package com.yiling.hmc.order.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.order.bo.OrderDetailControlBO;
import com.yiling.hmc.order.entity.OrderDetailControlDO;

/**
 * <p>
 * 订单明细管控表 服务类
 * </p>
 *
 * @author fan.shen
 * @date 2022-05-18
 */
public interface OrderDetailControlService extends BaseService<OrderDetailControlDO> {

    /**
     * 根据订单id和规格id查询订单明细管控信息
     *
     * @param orderId 订单id
     * @param sellSpecificationsId 商品规格id
     * @return 订单明细管控信息
     */
    List<OrderDetailControlDO> listByOrderIdAndSellSpecificationsId(Long orderId, Long sellSpecificationsId);

    /**
     * 根据订单id和规格id查询订单明细管控信息
     *
     * @param orderId 订单id
     * @param sellSpecificationsIdList 商品规格id
     * @return 管控渠道信息
     */
    List<OrderDetailControlBO> listByOrderIdAndSellSpecificationsIdList(Long orderId, List<Long> sellSpecificationsIdList);
}
