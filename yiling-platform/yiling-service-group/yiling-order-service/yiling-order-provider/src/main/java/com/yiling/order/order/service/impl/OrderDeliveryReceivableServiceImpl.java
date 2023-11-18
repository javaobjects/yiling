package com.yiling.order.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.order.order.dao.OrderDeliveryReceivableMapper;
import com.yiling.order.order.entity.OrderDeliveryReceivableDO;
import com.yiling.order.order.service.OrderDeliveryReceivableService;

/**
 * <p>
 * erp应收单和出库单关系表 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2021-09-23
 */
@Service
public class OrderDeliveryReceivableServiceImpl extends BaseServiceImpl<OrderDeliveryReceivableMapper, OrderDeliveryReceivableDO> implements OrderDeliveryReceivableService {

    @Override
    public boolean deleteOrderDeliveryReceivable(Long orderId, Long userId) {

        // 清除erp发货数据
        QueryWrapper<OrderDeliveryReceivableDO> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().eq(OrderDeliveryReceivableDO::getOrderId, orderId);
        OrderDeliveryReceivableDO OrderDeliveryErpDo = new OrderDeliveryReceivableDO();
        OrderDeliveryErpDo.setOpUserId(userId);
        // 清除发货数据
        this.batchDeleteWithFill(OrderDeliveryErpDo, deleteWrapper);
        return true;
    }

    /**
     * 根据订单id查询所有
     *
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderDeliveryReceivableDO> listByOrderIds(List<Long> orderIds) {
        QueryWrapper<OrderDeliveryReceivableDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderDeliveryReceivableDO :: getOrderId,orderIds);
        return list(wrapper);
    }

    /**
     * 根据应收单号获取出库单号
     *
     * @param erpReceivableNo 应收单号
     * @return
     */
    @Override
    public List<OrderDeliveryReceivableDO> getDeliveryNo(String erpReceivableNo) {
        QueryWrapper<OrderDeliveryReceivableDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDeliveryReceivableDO :: getErpReceivableNo,erpReceivableNo)
                .eq(OrderDeliveryReceivableDO :: getErpReceivableFlag,0);

        return list(wrapper);
    }

    @Override
    public boolean deleteDeliveryReceivableByOrderNo(String erpOrderNo) {
        // 清除发货数据
        QueryWrapper<OrderDeliveryReceivableDO> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().eq(OrderDeliveryReceivableDO :: getErpDeliveryNo,erpOrderNo);
        OrderDeliveryReceivableDO orderDeliveryReceivableDo = new OrderDeliveryReceivableDO();
        orderDeliveryReceivableDo.setOpUserId(0l);
        // 清除发货数据
        this.batchDeleteWithFill(orderDeliveryReceivableDo, deleteWrapper);

        return true;
    }
}
