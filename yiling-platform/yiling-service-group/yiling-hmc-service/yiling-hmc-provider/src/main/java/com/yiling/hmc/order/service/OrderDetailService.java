package com.yiling.hmc.order.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.order.entity.OrderDetailDO;

/**
 * <p>
 * 订单明细表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
public interface OrderDetailService extends BaseService<OrderDetailDO> {

    /**
     * 根据订单id查询订单明细
     *
     * @param orderId 订单id
     * @return 订单明细
     */
    List<OrderDetailDO> listByOrderId(Long orderId);

}
