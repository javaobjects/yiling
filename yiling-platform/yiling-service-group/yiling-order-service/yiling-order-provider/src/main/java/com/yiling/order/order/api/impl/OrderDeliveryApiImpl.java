package com.yiling.order.order.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderDeliveryApi;
import com.yiling.order.order.bo.OrderFlowBO;
import com.yiling.order.order.dto.OrderDeliveryDTO;
import com.yiling.order.order.dto.request.QueryOrderFlowRequest;
import com.yiling.order.order.entity.OrderDeliveryDO;
import com.yiling.order.order.service.OrderDeliveryService;

/**
 * 订单发货api查询
 * @author:wei.wang
 * @date:2021/6/22
 */
@DubboService
public class OrderDeliveryApiImpl implements OrderDeliveryApi {
    @Autowired
    private OrderDeliveryService orderDeliveryService;
    /**
     * 获取发货批次信息批量
     *
     * @param orderId
     * @return
     */
    @Override
    public List<OrderDeliveryDTO> getOrderDeliveryList(Long orderId) {
        List<OrderDeliveryDO> deliveryList = orderDeliveryService.getOrderDeliveryList(orderId);
        return PojoUtils.map(deliveryList,OrderDeliveryDTO.class);
    }

    @Override
    public List<OrderDeliveryDTO> getOrderDeliveryList(Long orderId, Long detailId) {
        QueryWrapper<OrderDeliveryDO> orderReturnDetailDOlWrapper = new QueryWrapper<>();
        orderReturnDetailDOlWrapper.lambda().eq(OrderDeliveryDO::getOrderId, orderId);
        orderReturnDetailDOlWrapper.lambda().eq(OrderDeliveryDO::getDetailId, detailId);
        return PojoUtils.map(orderDeliveryService.list(orderReturnDetailDOlWrapper), OrderDeliveryDTO.class);
    }

    /**
     * 获取发货信息
     *
     * @param orderIds
     * @return
     */
    @Override
    public List<OrderDeliveryDTO> listByOrderIds(List<Long> orderIds) {
        List<OrderDeliveryDO> list = orderDeliveryService.listByOrderIds(orderIds);
        return PojoUtils.map(list,OrderDeliveryDTO.class);
    }

    @Override
    public Page<OrderFlowBO> getPageList(QueryOrderFlowRequest request) {
        return orderDeliveryService.getPageList(request);
    }
}
