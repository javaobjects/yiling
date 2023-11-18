package com.yiling.order.order.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.entity.OrderDeliveryReceivableDO;

/**
 * <p>
 * erp应收单和出库单关系表 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2021-09-23
 */
public interface OrderDeliveryReceivableService extends BaseService<OrderDeliveryReceivableDO> {

    /**
     * 删除erp发货信息
     * @param orderId
     * @param userId
     * @return
     */
    boolean deleteOrderDeliveryReceivable(Long orderId, Long userId) ;


    /**
     * 根据订单id查询所有
     * @param orderIds
     * @return
     */
    List<OrderDeliveryReceivableDO> listByOrderIds(List<Long> orderIds);

    /**
     * 根据应收单号获取出库单号
     * @param erpReceivableNo 应收单号
     * @return
     */
    List<OrderDeliveryReceivableDO> getDeliveryNo(String erpReceivableNo);


    /**
     * 根据ERP订单号删除
     * @param erpOrderNo
     * @return
     */
    boolean deleteDeliveryReceivableByOrderNo(String erpOrderNo);

}
