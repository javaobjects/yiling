package com.yiling.order.order.api.impl;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.order.order.api.OrderExtendApi;
import com.yiling.order.order.dto.OrderB2BCountAmountDTO;
import com.yiling.order.order.dto.OrderExtendDTO;
import com.yiling.order.order.dto.request.CreateOrderExtendRequest;
import com.yiling.order.order.dto.request.QueryB2BOrderCountRequest;
import com.yiling.order.order.entity.OrderExtendDO;
import com.yiling.order.order.service.OrderExtendService;

/** 订单扩展属性表
 * @author zhigang.guo
 * @date: 2022/11/3
 */
@DubboService
public class OrderExtendApiImpl implements OrderExtendApi {

    @Autowired
    private OrderExtendService orderExtendService;

    @Override
    public boolean save(CreateOrderExtendRequest createOrderExtendRequest) {

        OrderExtendDO orderExtendDO = PojoUtils.map(createOrderExtendRequest, OrderExtendDO.class);

        return orderExtendService.save(orderExtendDO);
    }

    @Override
    public OrderExtendDTO getOrderExtendInfo(Long orderId) {


        return PojoUtils.map(orderExtendService.getOrderExtendInfo(orderId),OrderExtendDTO.class);
    }

    @Override
    public OrderB2BCountAmountDTO getB2BCountAmount(QueryB2BOrderCountRequest request) {
        return orderExtendService.getB2BCountAmount(request);
    }

    @Override
    public Integer getB2BCountQuantity(QueryB2BOrderCountRequest request) {
        return orderExtendService.getB2BCountQuantity(request);
    }
}
