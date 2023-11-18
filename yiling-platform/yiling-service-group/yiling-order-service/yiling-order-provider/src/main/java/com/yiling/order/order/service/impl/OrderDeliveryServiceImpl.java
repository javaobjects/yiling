package com.yiling.order.order.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.order.order.bo.OrderFlowBO;
import com.yiling.order.order.dao.OrderDeliveryMapper;
import com.yiling.order.order.dto.request.QueryOrderFlowRequest;
import com.yiling.order.order.entity.OrderDeliveryDO;
import com.yiling.order.order.service.OrderDeliveryService;

/**
 * <p>
 * 订单发货信息 服务实现类
 * </p>
 *
 * @author xuan.zhou
 * @date 2021-06-17
 */
@Service
public class OrderDeliveryServiceImpl extends BaseServiceImpl<OrderDeliveryMapper, OrderDeliveryDO> implements OrderDeliveryService {

    /**
     * 获取发货批次信息
     *
     * @param orderId
     * @param orderDetailId
     * @param goodsId
     * @return
     */
    @Override
    public List<OrderDeliveryDO> getOrderDeliveryInfo(Long orderId, Long orderDetailId, Long goodsId) {
        QueryWrapper<OrderDeliveryDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDeliveryDO::getOrderId, orderId)
                .eq(OrderDeliveryDO::getDetailId, orderDetailId).eq(OrderDeliveryDO::getGoodsId, goodsId)
                .eq(OrderDeliveryDO::getDelFlag,0);

        return list(wrapper);
    }

    /**
     * 获取发货批次信息批量
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderDeliveryDO> getOrderDeliveryList(Long orderId) {
        QueryWrapper<OrderDeliveryDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDeliveryDO::getOrderId, orderId);

        return list(wrapper);
    }

    /**
     * 获取发货数据
     *
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderDeliveryDO> listByOrderIds(List<Long> orderIds) {
        QueryWrapper<OrderDeliveryDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderDeliveryDO::getOrderId, orderIds);
        return list(wrapper);
    }

    /**
     * 根据订单编号，订单明细编号和批次号查询发货批次信息
     *
     * @param orderId
     * @param detailId
     * @param batchNo
     * @return
     */
    @Override
    public OrderDeliveryDO queryByOrderIdAndDetailIdAndBatchNo(Long orderId, Long detailId, String batchNo) {
        QueryWrapper<OrderDeliveryDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderDeliveryDO::getOrderId, orderId);
        wrapper.lambda().eq(OrderDeliveryDO::getDetailId, detailId);
        wrapper.lambda().eq(OrderDeliveryDO::getBatchNo, batchNo);
        wrapper.lambda().eq(OrderDeliveryDO::getDelFlag, 0);
        wrapper.last("LIMIT 1");
        return this.getOne(wrapper);
    }

    @Override
    public boolean deleteOrderDelivery(Long orderId, Long userId) {

        // 清除发货数据
        QueryWrapper<OrderDeliveryDO> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().eq(OrderDeliveryDO::getOrderId, orderId);
        OrderDeliveryDO orderDeliveryDO = new OrderDeliveryDO();
        orderDeliveryDO.setOpUserId(userId);
        // 清除发货数据
        this.batchDeleteWithFill(orderDeliveryDO, deleteWrapper);

        return true;
    }


    @Override
    public boolean deleteOrderDeliveryByPrimaryKey(List<Long> keyList,Long userId) {
        // 清除发货数据
        QueryWrapper<OrderDeliveryDO> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.lambda().in(OrderDeliveryDO::getId, keyList);
        OrderDeliveryDO orderDeliveryDO = new OrderDeliveryDO();
        orderDeliveryDO.setOpUserId(userId);
        // 清除发货数据
        this.batchDeleteWithFill(orderDeliveryDO, deleteWrapper);

        return true;
    }

    @Override
    public Page<OrderFlowBO> getPageList(QueryOrderFlowRequest request) {
        return this.baseMapper.getPageList(new Page<>(request.getCurrent(), request.getSize()),request);
    }
}
