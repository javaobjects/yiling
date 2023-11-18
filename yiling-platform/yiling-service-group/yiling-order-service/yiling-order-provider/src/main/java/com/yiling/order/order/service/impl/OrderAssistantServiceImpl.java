package com.yiling.order.order.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.order.order.dto.request.OrderB2BPageRequest;
import com.yiling.order.order.dto.request.QueryAssistantFirstOrderRequest;
import com.yiling.order.order.entity.OrderDO;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.order.order.enums.OrderStatusEnum;
import com.yiling.order.order.enums.OrderTypeEnum;
import com.yiling.order.order.service.OrderAssistantService;
import com.yiling.order.order.service.OrderService;

/**
 * 销售助手service
 *
 * @author:wei.wang
 * @date:2021/9/22
 */

@Service
public class OrderAssistantServiceImpl implements OrderAssistantService {
    @Autowired
    private OrderService orderService;


    /**
     * 全部订单查询接口
     *
     * @param request
     * @return
     */
    @Override
    public Page<OrderDO> getPOPOrderList(OrderB2BPageRequest request) {
        QueryWrapper<OrderDO> wrapper = new QueryWrapper<>();
        Integer type = request.getType();
        String condition = request.getCondition();
        if (1 == type) {
            //待审核
            wrapper.lambda().eq(OrderDO::getAuditStatus, OrderAuditStatusEnum.UNAUDIT.getCode()).eq(OrderDO::getOrderStatus, OrderStatusEnum.UNAUDITED.getCode());
        } else if (2 == type) {
            //待发货
            wrapper.lambda().eq(OrderDO::getOrderStatus, OrderStatusEnum.UNDELIVERED.getCode());
        } else if (3 == type) {
            //待收货
            wrapper.lambda().eq(OrderDO::getOrderStatus, OrderStatusEnum.DELIVERED.getCode());
        } else if (4 == type) {
            //已完成
            wrapper.lambda().ge(OrderDO::getOrderStatus, OrderStatusEnum.RECEIVED.getCode());
        } else if (5 == type) {
            //已取消
            wrapper.lambda().eq(OrderDO::getOrderStatus, OrderStatusEnum.CANCELED.getCode());
        }

        if (request.getSellerEid() != null && request.getSellerEid() != 0) {
            wrapper.lambda().eq(OrderDO::getBuyerEid, request.getSellerEid());
        }
        wrapper.lambda().eq(OrderDO::getOrderType, OrderTypeEnum.POP.getCode()).eq(OrderDO::getBuyerEid, request.getEid()).orderByDesc(OrderDO::getCreateTime);
        if (StringUtils.isNotEmpty(condition)) {
            wrapper.lambda().like(OrderDO::getOrderNo, condition);
        }
        Page<OrderDO> page = orderService.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        return page;
    }

    @Override
    public OrderDO getFirstOrder(QueryAssistantFirstOrderRequest request) {
        LambdaQueryWrapper<OrderDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(OrderDO::getBuyerEid,request.getBuyerEid());
        wrapper.eq(OrderDO::getOrderSource, OrderSourceEnum.SA.getCode()).eq(OrderDO::getOrderStatus, OrderStatusEnum.RECEIVED.getCode()).last("limit 1");
        OrderDO orderDO = orderService.getOne(wrapper);
        return orderDO;
    }
}
