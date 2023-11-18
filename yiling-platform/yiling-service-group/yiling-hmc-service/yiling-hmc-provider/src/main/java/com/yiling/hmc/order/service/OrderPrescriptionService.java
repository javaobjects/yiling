package com.yiling.hmc.order.service;

import com.yiling.framework.common.base.BaseService;
import com.yiling.hmc.order.dto.OrderPrescriptionDTO;
import com.yiling.hmc.order.entity.OrderPrescriptionDO;

/**
 * <p>
 * C端兑付订单关联处方表 服务类
 * </p>
 *
 * @author yong.zhang
 * @date 2022-03-25
 */
public interface OrderPrescriptionService extends BaseService<OrderPrescriptionDO> {

    /**
     * 获取处方
     *
     * @param orderId
     * @return
     */
    OrderPrescriptionDTO getByOrderId(Long orderId);
}
