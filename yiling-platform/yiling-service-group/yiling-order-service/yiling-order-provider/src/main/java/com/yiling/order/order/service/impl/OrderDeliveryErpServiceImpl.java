package com.yiling.order.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.order.order.dao.OrderDeliveryErpMapper;
import com.yiling.order.order.entity.OrderDeliveryErpDO;
import com.yiling.order.order.service.OrderDeliveryErpService;

/**
 * <p>
 * 订单ERP出库单信息 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2021-09-23
 */
@Service
public class OrderDeliveryErpServiceImpl extends BaseServiceImpl<OrderDeliveryErpMapper, OrderDeliveryErpDO> implements OrderDeliveryErpService {

    @Override
    public boolean deleteErpOrderDelivery(Long orderId, Long userId) {

        // 清除erp发货数据
        QueryWrapper<OrderDeliveryErpDO> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().eq(OrderDeliveryErpDO::getOrderId, orderId);
        OrderDeliveryErpDO OrderDeliveryErpDo = new OrderDeliveryErpDO();
        OrderDeliveryErpDo.setOpUserId(userId);
        // 清除发货数据
        this.batchDeleteWithFill(OrderDeliveryErpDo, deleteWrapper);

        return true;
    }
    @Override
    public List<OrderDeliveryErpDO> listByDetailIdAndBatchNo(Long orderId, Long detailId, String batchNo) {
        QueryWrapper<OrderDeliveryErpDO> wrapper = new QueryWrapper<>();
        if (null != orderId) {
            wrapper.lambda().eq(OrderDeliveryErpDO::getOrderId, orderId);
        }
        wrapper.lambda().eq(OrderDeliveryErpDO::getDetailId, detailId)
                .eq(OrderDeliveryErpDO::getBatchNo, batchNo)
                .orderByDesc(OrderDeliveryErpDO::getDeliveryQuantity);
        return this.list(wrapper);
    }

    @Override
    public Integer selectErporderDeliverySize(Long orderId) {

        QueryWrapper<OrderDeliveryErpDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDeliveryErpDO::getOrderId,orderId);

        return this.baseMapper.selectCount(wrapper);
    }

    /**
     * 根据订单id查询所有
     *
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderDeliveryErpDO> listByOrderIds(List<Long> orderIds) {
        QueryWrapper<OrderDeliveryErpDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderDeliveryErpDO :: getOrderId,orderIds);
        return list(wrapper);
    }

    /**
     * 根据订单查询
     *
     * @param detailId
     * @param erpDeliveryNo
     * @return
     */
    @Override
    public List<OrderDeliveryErpDO> listByDetailIdAndNo(Long detailId, String erpDeliveryNo) {
        QueryWrapper<OrderDeliveryErpDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDeliveryErpDO :: getErpDeliveryNo,erpDeliveryNo)
                .eq(OrderDeliveryErpDO :: getDetailId,detailId);
        return list(wrapper);
    }


    @Override
    public boolean deleteDeliveryErpByErpOrderNo(String erpOrderNo,String batchNo) {

        // 清除发货数据
        QueryWrapper<OrderDeliveryErpDO> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().eq(OrderDeliveryErpDO :: getErpDeliveryNo,erpOrderNo);
        deleteWrapper.lambda().eq(OrderDeliveryErpDO :: getBatchNo,batchNo);
        OrderDeliveryErpDO orderDeliveryDO = new OrderDeliveryErpDO();
        orderDeliveryDO.setOpUserId(0l);
        // 清除发货数据
        this.batchDeleteWithFill(orderDeliveryDO, deleteWrapper);

        return true;
    }
}
