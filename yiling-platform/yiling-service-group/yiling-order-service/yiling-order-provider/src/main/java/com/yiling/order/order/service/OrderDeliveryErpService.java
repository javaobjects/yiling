package com.yiling.order.order.service;

import java.util.List;

import com.yiling.framework.common.base.BaseService;
import com.yiling.order.order.entity.OrderDeliveryErpDO;

/**
 * <p>
 * 订单ERP出库单信息 服务类
 * </p>
 *
 * @author wei.wang
 * @date 2021-09-23
 */
public interface OrderDeliveryErpService extends BaseService<OrderDeliveryErpDO> {
    /**
     * 根据订单明细id和批次号查询erp出库单信息
     *
     * @param detailId
     * @param batchNo
     * @return
     */
    List<OrderDeliveryErpDO> listByDetailIdAndBatchNo(Long orderId, Long detailId, String batchNo);

    /**
     * 删除erp发货信息
     * @param orderId
     * @param userId
     * @return
     */
    boolean deleteErpOrderDelivery(Long orderId, Long userId) ;

    /**
     * 查询订单发货数量
     * @param orderId
     * @return
     */
    Integer selectErporderDeliverySize(Long orderId);

    /**
     * 根据订单id查询所有
     * @param orderIds
     * @return
     */
    List<OrderDeliveryErpDO> listByOrderIds(List<Long> orderIds);

    /**
     * 根据订单查询
     * @param detailId
     * @param erpDeliveryNo
     * @return
     */
    List<OrderDeliveryErpDO> listByDetailIdAndNo(Long detailId,String erpDeliveryNo);


    /**
     * 根据ERP订单号删除
     * @param erpOrderNo,batchNo
     * @return
     */
    boolean deleteDeliveryErpByErpOrderNo(String erpOrderNo,String batchNo);

}
