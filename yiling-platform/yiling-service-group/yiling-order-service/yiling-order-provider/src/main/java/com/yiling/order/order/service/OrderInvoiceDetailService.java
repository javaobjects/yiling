package com.yiling.order.order.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.entity.OrderInvoiceDetailDO;

/**
 * <p>
 * 订单开票明细
 * </p>
 *
 * @author wei.wang
 * @date 2021-08-04
 */

public interface OrderInvoiceDetailService extends BaseService<OrderInvoiceDetailDO> {



    /**
     * 根据订单ids获取开票申请明细信息
     * @param orders
     * @return
     */
    List<OrderInvoiceDetailDO> listByOrderIds(List<Long> orders);
}
